# Face Recognizer

### Note: blink check
Using **Geometric method** called the **Eye Aspect Ratio (EAR)**
- Facial Landmarks: Using **Dlib's 68-point face landmark model**, the code identifies 6 specific points around each eye
    - Points: corners, top, and bottom of the eye
    - These are mapped using
    ```python
    (lStart, lEnd) = face_utils.FACIAL_LANDMARKS_IDXS["left_eye"]
    (rStart, rEnd) = face_utils.FACIAL_LANDMARKS_IDXS["right_eye"]
    ```
- EAR calculation: measure the "openness" of the eye using vertical and horizontal distances
between the landmark point:
    ```python
    A = dist.euclidean(eye[1], eye[5])  # vertical
    B = dist.euclidean(eye[2], eye[4])  # vertical
    C = dist.euclidean(eye[0], eye[3])  # horizontal
    EAR = (A + B) / (2.0 * C)
    ```
  This value is high when the eye is open and drops significantly when the eye closes
- Blink detection logic:
  - If EAR drops below a **threshold** (e.g. `0.23`), it's considered a closed eye
  - The system waits until the EAR drops for a few consecutive frames (e.g. `3`) to confirm
it's not a noise or quick movement
    ```python
    if ear < BLINK_THRESHOLD:
    blink_counter += 1
    else:
    if blink_counter >= CONSEC_FRAMES:
    blink_detected_flag = True  # Blink confirmed
    blink_counter = 0
    ```

### Face data collection via [`datacollect.py`](datacollect.py)

The function `collect_face_data(name)` captures and stores face images:
- **Face detection**: uses openCV's Haarcascade classifier ([`harrcascade_frontalface_default.xml`](harrcascade_frontalface_default.xml)) to detect faces
- **Liveness check**: A _blink detection_ mechanism ensures that the face is live (not a photo).
It computes the EAR using Dlib’s 68-point facial landmark detector and waits for a valid blink (EAR drops below a threshold for consecutive frames) `datacollect`
- **Image saving**: Once a blink is detected, it captures and resizes face images (200x200 px) and stores them as `.jpg` files in a subdirectory under `datasets/`, named after the person `datacollect`

### Face recognition handling with [`testmodel.py`](testmodel.py)
The function `recognize_face` performs face identification:
- **Live face detection**: Again, it uses blink detection to confirm the presence of a live face `testmodel`
- **Recognition with DeepFace**:\
  - Once a blink is confirmed, it captures the face and compares it against stored images using DeepFace’s `find()` method
  - The comparison is done against the `datasets/` directory.
  - If a match is found, the model extracts the person's name from the directory structure of the matched file path `testmodel`.

### Data storage

- Images are stored as `.jpg` files on disk under `datasets/{person_name}/`

### Flask API implemented in [`main.py`](main.py)

- A simple Flask server wraps above functionalities with 2 APIs:
  - `POST /datacollect`: triggers face data collection for a given name in request body
  - `GET /testmodel`: runs the recognition process and returns the detected name if found in `datasets`

