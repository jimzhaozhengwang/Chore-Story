# Backbone
Runs on a single machine using Docker-compose to manage it's services
Backend uses `python3.7`

Services:
 * SQLite
 * Our Flask service: `backbone`

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
    cd server/backbone
    python3.7 -m pip install -r server/backbone/requirements.txt
    ```
4. Build the necessary database
    ```bash
    cd ..
    FLASK_APP=backbone flask init-db
    ```
    Note: Flask command needs to be run from the server directory.

## Generating Backbone documentation

1. Go into Backbone directory
    ```bash
    cd server/backbone
    ```
2. Generate documentation using Sphinx
    * pdf (make sure you have Latex installed)
    ```bash
    make pdflatex
    ```
    * html
    ```bash
    make html
    ```
    Output will be in on of `_build` sub-directories.