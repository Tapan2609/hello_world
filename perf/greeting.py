from locust import HttpLocust, TaskSet, task, between

class MyTaskSet(TaskSet):
    @task
    def greeting(self):
        self.client.get("/greeting")
    def on_start(self):
        self.client.verify = False

class MyLocust(HttpLocust):
    task_set = MyTaskSet
    wait_time = between(2, 3)
