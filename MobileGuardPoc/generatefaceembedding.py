# generate embedding vector from a face photo using facenet.tflite
# Usage: python generatefaceembedding.py --photo usertoimpersonate.jpg --model assets/facenet.tflite --save-embedding impersonate_emb.json

import argparse
import json
import sys
import time
import numpy as np
from PIL import Image

FACENET_INPUT_SIZE = 160
FACENET_NORM_MEAN = 127.5
FACENET_NORM_STD = 128.0
FACENET_EMBEDDING_DIM = 512

def load_and_preprocess_face(image_path: str) -> np.ndarray:

    print(f"[*] Loading photo: {image_path}")
    img = Image.open(image_path).convert("RGB")
    # ResizeOp(160, 160, BILINEAR)
    img = img.resize((FACENET_INPUT_SIZE, FACENET_INPUT_SIZE), Image.BILINEAR)
    # Convert to numpy float32 array
    img_array = np.array(img, dtype=np.float32)
    # NormalizeOp(mean=127.5, stddev=128.0)
    # Formula: (pixel - mean) / stddev
    img_array = (img_array - FACENET_NORM_MEAN) / FACENET_NORM_STD
    # Add batch dimension: [1, 160, 160, 3]
    img_array = np.expand_dims(img_array, axis=0)
    print(f"[+] Preprocessed image shape: {img_array.shape}, "
          f"range: [{img_array.min():.3f}, {img_array.max():.3f}]")
    return img_array

def generate_embedding(model_path: str, image_path: str) -> list:
    """
    Replicates FaceEmbedder.getEmbedding():
      1. Preprocess the face image
      2. Run TFLite FaceNet inference
      3. Return 512-dim float embedding vector
    """
    try:
        import tensorflow as tf
    except ImportError:
        # Fallback: try tflite_runtime (lighter package)
        try:
            import tflite_runtime.interpreter as tflite
            print("[*] Using tflite_runtime")
            return _generate_embedding_tflite_runtime(tflite, model_path, image_path)
        except ImportError:
            print("[!] ERROR: Install tensorflow or tflite-runtime:")
            print("    pip install tensorflow")
            print("    # or: pip install tflite-runtime")
            sys.exit(1)
    print(f"[*] Loading FaceNet model: {model_path}")
    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    print(f"[+] Model input:  {input_details[0]['shape']} {input_details[0]['dtype']}")
    print(f"[+] Model output: {output_details[0]['shape']} {output_details[0]['dtype']}")
    # Preprocess
    input_data = load_and_preprocess_face(image_path)
    # Run inference (replicates: interpreter.run(process.getBuffer(), embeddingBuffer))
    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()
    # Extract 512-dim embedding
    embedding = interpreter.get_tensor(output_details[0]['index'])[0]
    print(f"[+] Generated embedding: {FACENET_EMBEDDING_DIM}-dim vector")
    print(f"    First 5 values: {embedding[:5].tolist()}")
    print(f"    L2 norm: {np.linalg.norm(embedding):.4f}")
    return embedding.tolist()

def _generate_embedding_tflite_runtime(tflite, model_path, image_path):
    """Fallback using tflite_runtime instead of full tensorflow."""
    print(f"[*] Loading FaceNet model: {model_path}")
    interpreter = tflite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    input_data = load_and_preprocess_face(image_path)
    interpreter.set_tensor(input_details[0]['index'], input_data)
    interpreter.invoke()
    embedding = interpreter.get_tensor(output_details[0]['index'])[0]
    print(f"[+] Generated embedding: {FACENET_EMBEDDING_DIM}-dim vector")
    return embedding.tolist()

def main():
    parser = argparse.ArgumentParser(
        description="Face Embedding Generator",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
            python faceembedding.py --photo usertoimpersonate.jpg --model facenet.tflite --save-embedding impersonate_emb.json
           """,
                )
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--photo", help="Path to the target user's face photo")
    parser.add_argument("--model", default="facenet.tflite",
                        help="Path to facenet.tflite (default: facenet.tflite)")
    parser.add_argument("--save-embedding", metavar="FILE",
                        help="Save the extracted embedding to a JSON file for reuse")

    args = parser.parse_args()

    print(f"\n[*] Phase 0: Extracting FaceNet embedding from photo")
    embedding = generate_embedding(args.model, args.photo)
    
    with open(args.save_embedding, "w") as f:
        json.dump(embedding, f)
    print(f"[+] Embedding saved to: {args.save_embedding}")

    print(f"{'='*60}")

if __name__ == "__main__":
    main()