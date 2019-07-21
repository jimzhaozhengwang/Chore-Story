import dialogflow_v2 as dialogflow
from flask import current_app
from flask_login import current_user
from google.protobuf.json_format import MessageToDict


def call_dialogflow(text):
    project_id = current_app.config.get('DIALOGFLOW_PROJECT_ID')
    if project_id:
        session_id = current_user.name
        session_client = dialogflow.SessionsClient()
        session = session_client.session_path(project_id, session_id)

        text_input = dialogflow.types.TextInput(
            text=text, language_code='en')

        query_input = dialogflow.types.QueryInput(text=text_input)

        response = session_client.detect_intent(
            session=session, query_input=query_input)

        return MessageToDict(response.query_result.parameters)
    return None
