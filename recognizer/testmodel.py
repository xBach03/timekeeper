from deepface import DeepFace
import cv2
import os
import dlib
from imutils import face_utils
from scipy.spatial import distance as dist

# Constants for blink detection
blink_counter = 0
BLINK_THRESHOLD = 0.23
CONSEC_FRAMES = 3
blink_detected_flag = False

def eye_aspect_ratio(eye):
    A = dist.euclidean(eye[1], eye[5])
    B = dist.euclidean(eye[2], eye[4])
    C = dist.euclidean(eye[0], eye[3])
    return (A + B) / (2.0 * C)

# Load detectors
face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')
detector = dlib.get_frontal_face_detector()
predictor = dlib.shape_predictor("shape_predictor_68_face_landmarks.dat")

# Dataset path
dataset_path = "datasets"

# Webcam
video = cv2.VideoCapture(0)

while True:
    ret, frame = video.read()
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    faces = face_cascade.detectMultiScale(gray, 1.3, 5)

    for (x, y, w, h) in faces:
        face_img = frame[y:y + h, x:x + w]
        rect = dlib.rectangle(int(x), int(y), int(x + w), int(y + h))
        shape = predictor(gray, rect)
        shape = face_utils.shape_to_np(shape)

        # EAR
        (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
        (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]
        leftEye = shape[lStart:lEnd]
        rightEye = shape[rStart:rEnd]
        ear = (eye_aspect_ratio(leftEye) + eye_aspect_ratio(rightEye)) / 2.0

        # Multi-frame blink detection
        if ear < BLINK_THRESHOLD:
            blink_counter += 1
        else:
            if blink_counter >= CONSEC_FRAMES:
                blink_detected_flag = True
            blink_counter = 0

        # Only recognize after a blink
        if blink_detected_flag:
            try:
                result = DeepFace.find(img_path=face_img, db_path=dataset_path, enforce_detection=False)
                if not result[0].empty:
                    name = os.path.basename(os.path.dirname(result[0].iloc[0]["identity"]))
                    print(f" Recognized as: {name}")
                else:
                    name = "Unknown"
            except Exception as e:
                print("Recognition error:", e)
                name = "Error"

            cv2.putText(frame, "Blink detected", (x, y - 30),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)
            blink_detected_flag = False
        else:
            name = ""

        # Draw results
        cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
        cv2.putText(frame, name, (x, y - 10),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.9, (255, 255, 255), 2)

    cv2.imshow("Face Recognition with Blink Detection", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

video.release()
cv2.destroyAllWindows()
