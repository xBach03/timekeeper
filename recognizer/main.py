from flask import Flask, request, jsonify
from datacollect import collect_face_data
from testmodel import recognize_face

app = Flask(__name__)


@app.route("/datacollect", methods=["POST"])
def api_datacollect():
    name = request.form.get("name") or (request.get_json(silent=True) or {}).get("name")
    if not name:
        return jsonify({"status": "error", "message": "Missing 'name' parameter", "name": name}), 400
    result = collect_face_data(name)
    return jsonify({"status": "success", "message": result, "name": name})


@app.route("/testmodel", methods=["GET"])
def api_testmodel():
    result = recognize_face()
    return jsonify(result)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8000)
