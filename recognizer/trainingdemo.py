import cv2
import numpy as np
from PIL import Image
import os
import json

recognizer = cv2.face.LBPHFaceRecognizer_create()
path = "datasets"

# Dictionary to store mapping between string IDs and numeric labels
id_mapping = {}

def getImageID(path):
    imagePaths = [os.path.join(path, f) for f in os.listdir(path)]
    faces = []
    ids = []
    unique_id = 0  # Start numeric ID from 0

    for imagePath in imagePaths:
        faceImage = Image.open(imagePath).convert('L')
        faceNP = np.array(faceImage)
        
        # Extract string ID from filename (Assuming format: "user.stringID.number.jpg")
        string_id = os.path.split(imagePath)[-1].split(".")[1]  # Extract the second part as string ID
        
        # Assign a unique integer ID if this string_id is new
        if string_id not in id_mapping:
            id_mapping[string_id] = unique_id
            unique_id += 1

        numeric_id = id_mapping[string_id]  # Get the corresponding numeric ID

        faces.append(faceNP)
        ids.append(numeric_id)  # Use the numeric ID for training

        cv2.imshow("Training", faceNP)
        cv2.waitKey(1)

    return ids, faces

# Get numeric IDs and face data
IDs, facedata = getImageID(path)

# Convert to NumPy array (must be int32 for OpenCV)
IDs = np.array(IDs, dtype=np.int32)

# Train the recognizer
recognizer.train(facedata, IDs)
recognizer.write("Trainer.yml")
cv2.destroyAllWindows()

print("Training Completed............")
print("ID Mapping:", id_mapping)  # Print the mapping of string names to numeric IDs
with open("id_mapping.json", "w") as f:
    json.dump(id_mapping, f)

print("ID Mapping saved to id_mapping.json")