suspend fun main(args: Array<String>){
    /*coroutineScope {

        val testConf = config {
            addDefaultProduceConfig(UnitType.INVERTER, 0.5)
            addTypeConfig(UnitType.INVERTER, InverterType.HUAWEI) {
                UP_POWER_CONTROL_PER_TICK = 2.0
                READ_FREQUENCY = 6
            }
            addUnitConfig(UnitType.INVERTER, 1) {
                addDefVales {
                    hasError = false
                }
                addTask {
                    listOf(
                        DslStartErrorTask(10),
                        DslEndErrorTask(20)
                    )
                }
            }
        }

        LogFlags.UNIT_READ_LOG = true

        val simDa = SimulationData(
            powerPlants = mapOf(
                4 to PowerPlantData(4, "Test84", 22_000.0),
            ),
            inverters = mapOf(
                4 to InverterData(4, 4, 2_000.0, true, InverterType.FRONIUS),
                5 to InverterData(5, 4, 2_000.0, true, InverterType.FRONIUS),
            ),
            engines = mapOf(
                6 to EngineData(6, 4, 10_000.0,0.5,12, EngineType.TEST),
                7 to EngineData(7, 4, 8_000.0,0.5,12, EngineType.TEST),
            )
        )

        startKoin {}

        var i = 0

        val test = Simulation(simDa, 100, true)
        launch {
            test.runWithSave(60)
        }

        while(true){
            i += 2
            println(test.powerController.readParks())
            if(i < 30) {
                test.powerController.commandParks(mapOf(4 to 22_000))
            }else{
                test.powerController.commandParks(mapOf(4 to 10_000))
            }
            sleep(2_000)
        }
    }*/
}