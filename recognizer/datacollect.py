import cv2
import os
import dlib
from imutils import face_utils
from scipy.spatial import distance as dist

# Blink detection settings
BLINK_THRESHOLD = 0.23
CONSEC_FRAMES = 3


def eye_aspect_ratio(eye):
    A = dist.euclidean(eye[1], eye[5])
    B = dist.euclidean(eye[2], eye[4])
    C = dist.euclidean(eye[0], eye[3])
    return (A + B) / (2.0 * C)


def collect_face_data(name, max_samples=30, save_dir="datasets"):
    person_dir = os.path.join(save_dir, name)
    os.makedirs(person_dir, exist_ok=True)

    face_cascade = cv2.CascadeClassifier("haarcascade_frontalface_default.xml")
    predictor = dlib.shape_predictor("shape_predictor_68_face_landmarks.dat")

    cap = cv2.VideoCapture(0)
    count = 0
    blink_counter = 0
    liveness_passed = False

    print(f" Blink once to begin collecting data for {name}...")

    while True:
        ret, frame = cap.read()
        if not ret:
            break

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = face_cascade.detectMultiScale(gray, 1.3, 5)

        for (x, y, w, h) in faces:
            rect = dlib.rectangle(int(x), int(y), int(x + w), int(y + h))
            shape = predictor(gray, rect)
            shape = face_utils.shape_to_np(shape)

            (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
            (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]
            leftEye = shape[lStart:lEnd]
            rightEye = shape[rStart:rEnd]
            ear = (eye_aspect_ratio(leftEye) + eye_aspect_ratio(rightEye)) / 2.0

            # Blink detection
            if not liveness_passed:
                if ear < BLINK_THRESHOLD:
                    blink_counter += 1
                else:
                    if blink_counter >= CONSEC_FRAMES:
                        liveness_passed = True
                        print(" Blink detected! Starting data collection...")
                    blink_counter = 0
                msg = "Blink to Start"
            else:
                msg = f"Collecting: {count}/{max_samples}"

                # Save multiple images rapidly after liveness check
                for i in range(2):  # Capture 2 frames per loop
                    ret2, frame2 = cap.read()
                    if not ret2:
                        break
                    face_img = frame2[y:y + h, x:x + w]
                    resized_face = cv2.resize(face_img, (200, 200))
                    count += 1
                    cv2.imwrite(f"{person_dir}/{count}.jpg", resized_face)
                    if count >= max_samples:
                        break

            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
            cv2.putText(frame, msg, (x, y - 10),
                        cv2.FONT_HERSHEY_SIMPLEX, 0.7, (0, 255, 255), 2)

        cv2.imshow("Face Data Collection", frame)

        if cv2.waitKey(1) & 0xFF == ord('q') or count >= max_samples:
            break

    print(f"\n Collected {count} samples for {name}.")
    cap.release()
    cv2.destroyAllWindows()
    return f"Collected {count} samples for {name}"

