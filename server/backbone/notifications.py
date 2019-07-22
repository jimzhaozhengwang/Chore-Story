from pyfcm import FCMNotification
from abc import ABC, abstractmethod
from flask import current_app
from sqlalchemy import inspect as sa_inspect

from .models import Parent, Child


class Notification(ABC):
 
  def __init__(self, receiver, message_title, message_body):
    super().__init__()
    self.receiver = receiver
    self.message_title = message_title
    self.message_body = message_body
    self.api_key = current_app.config.get('NOTIF_API_KEY')

  def should_send_notification(self):
    return self.receiver.registration_id and self.receiver_should_get_notification() and self.api_key
  
  def send_notification(self):
    if (self.should_send_notification()):
      print(self.api_key)
      return FCMNotification(api_key=self.api_key).notify_single_device(registration_id=self.receiver.registration_id,
                                                                        message_title=self.message_title,
                                                                        message_body=self.message_body)
    else:
      return False

  @abstractmethod
  def receiver_should_get_notification(self):
    pass


class NewQuestNotification(Notification):

  def __init__(self, receiver, quest_name):
    message_title = "New Quest"
    message_body = "You have a new quest to complete: " + quest_name
    self.receiver = receiver
    super().__init__(receiver, message_title, message_body)

  def receiver_should_get_notification(self):
    return isinstance(sa_inspect(self.receiver).object, Child)
