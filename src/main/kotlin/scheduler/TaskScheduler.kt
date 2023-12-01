package scheduler

class TaskScheduler{

    private val taskList: MutableList<Task> = mutableListOf()

    fun addTask(task: Task){
        taskList.add(task)
    }

    fun removeTask(task: Task){
        taskList.remove(task)
    }

    fun checkTasks(){
        val iterator = taskList.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            if(task.tick() <= 0)
                iterator.remove()
        }
    }

    fun getTaskByType(taskType: TaskType): List<Task>{
        return taskList.filter { it.type == taskType }
    }

    /**
     * Delete every Task that are not Error or other that need to be added by config
     */
    fun emptyTaskList(){
        taskList.filter { it.type !in configTasks }.forEach {
            taskList.remove(it)
        }
    }

    companion object{
        val configTasks = listOf(
            TaskType.START_ERROR,
            TaskType.END_ERROR
        )
    }



}