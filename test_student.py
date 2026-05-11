import urllib.request
try:
    response = urllib.request.urlopen('http://localhost:8080/test-equipments')
    print(response.read().decode('utf-8'))
except Exception as e:
    print(e)
