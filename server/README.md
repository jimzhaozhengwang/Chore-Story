# Backbone
Runs on a single machine using Docker-compose to manage it's services
Backend uses `python3.7`

Services:
 * SQLite
 * our Flask service - backbone

## To run backbone
1. Clone repo
    ```bash
    git clone https://github.com/jimzhaozhengwang/CS446-Project.git cs446-project
    ```
2. Check out the backend dev branch (Temporary)
    ```bash
    cd cs446-project
    git checkout --track origin/newsletter
    ```
3. Install all of the dependencies(pinned in `requirement.txt`):
    ```bash
    python3.7 -m pip install -r backbone/backbone/requirements.txt
    ```
4. Build the necessary database
    ```bash
    cd backbone
    FLASK_APP=backbone flask init-db
    ```

