# Backbone
Runs on a single machine using Docker-compose to manage it's services
Backend uses `python3.7`

Services:
 * SQLite
 * Our Flask service: `backbone`

## To run backbone
1. Clone repo
    ```bash
    git clone https://github.com/jimzhaozhengwang/Chore-Story cs446-project
    cd cs446-project
    ```
2. Install `backbone`:
    ```bash
    cd server
    python3.7 -m pip install .
    ```
3. (Only if database models were changed) Generate database update 
    ```bash
    cd server/src/backbone
    alembic revision -m "updated db models" --autogenerate
    ```
4. Build the necessary database
   For a brand new database remove `db.sqlite` in `/server/src/backbone/` first.
   ```bash
   cd src/backbone
   alembic upgrade head
   ```
    Note: Flask command needs to be run from the `server/src` directory.

## Generating Backbone documentation

1. Go into Backbone directory
    ```bash
    cd server/src/backbone
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