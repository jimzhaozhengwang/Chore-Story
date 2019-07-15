from pyfcm import FCMNotification
from abc import ABC, abstractmethod
from .models import Parent, Child


class Notification(ABC):

  api_key = "AAAAuKgxco4:APA91bF0GiF-EGkdRLYXzZYIf76YQffw_93_GTut2N2pj48CS_Vdw44scWBzJqkobei5xcfE9IET2mXLIOGQJdOGQbhkal_A9BYfQNQ3L1AWcZnsIQdfoiku2htuETMOB2qzeehBQ2hE"
  push_service = FCMNotification(api_key=api_key)
 
  def __init__(self, receiver, message_title, message_body):
    self.receiver = receiver
    self.message_title = message_title
    self.message_body = message_body
    super().__init__()

  def should_send_notification(self):
    return self.receiver.registration_id and self.receiver_should_get_notification()
  
  def send_notification(self):
    if (self.should_send_notification()):
      return self.push_service.notify_single_device(registration_id=self.receiver.registration_id, message_title=self.message_title, message_body=self.message_body)
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
    return isinstance(self.receiver, Child)


class QuestProofNotification(Notification):

  def __init__(self, receiver, child, quest_name):
    message_title = "New proof to review"
    message_body = child.name + " provided proof for " + quest_name + "!"
    self.receiver = receiver
    super().__init__(receiver, message_title, message_body)

  def receiver_should_get_notification(self):
    return isinstance(self.receiver, Parent)
