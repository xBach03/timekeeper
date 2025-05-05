import cv2
import json
import numpy as np

# Load ID mapping from JSON
with open("id_mapping.json", "r") as f:
    id_mapping = json.load(f)

reverse_mapping = {v: k for k, v in id_mapping.items()}  # Reverse mapping

recognizer = cv2.face.LBPHFaceRecognizer_create()
recognizer.read("Trainer.yml")

facedetect = cv2.CascadeClassifier("haarcascade_frontalface_default.xml")

video = cv2.VideoCapture(0)

while True:
    ret, frame = video.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = facedetect.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        faceROI = cv2.resize(gray[y:y+h, x:x+w], (200, 200))
        serial, conf = recognizer.predict(faceROI)

        if conf < 70:  # Confidence threshold
            name = reverse_mapping.get(serial, "Unknown")
        else:
            name = "Unknown"

        cv2.rectangle(frame, (x, y), (x + w, y + h), (50, 50, 255), 2)
        cv2.rectangle(frame, (x, y - 40), (x + w, y), (50, 50, 255), -1)
        cv2.putText(frame, name, (x, y - 10), cv2.FONT_HERSHEY_SIMPLEX, 0.8, (255, 255, 255), 2)

    cv2.imshow("Face Recognition", frame)
    
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

video.release()
cv2.destroyAllWindows()
