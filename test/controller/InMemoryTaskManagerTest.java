package controller;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }


}
