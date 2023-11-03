package scheduler

class TaskScheduler(

) {

    private val taskList: MutableList<Task> = mutableListOf()

    fun addTask(task: Task){
        taskList.add(task)
    }

    fun addAllTask(vararg tasks: Task){
        tasks.forEach {
            taskList.add(it)
        }
    }

    fun checkTasks(){
        val iterator = taskList.iterator()
        while (iterator.hasNext()){
            val task = iterator.next()
            if(task.tick() <= 0)
                iterator.remove()
        }
    }



}