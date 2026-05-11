import urllib.request
import json

try:
    req = urllib.request.Request("http://localhost:8080/login")
    response = urllib.request.urlopen(req)
    print("App is running")
except Exception as e:
    print(f"Error: {e}")
