package units

class PodKif(
    podKifId: Int,
    private var isProducing: Boolean,
    private val maxPowerOut: Double
): AbstractUnit(podKifId, UnitType.KIF_POD) {

    override fun read(): UnitPower {
        return TODO()/* if (isProducing){
            maxPowerOut
        } else {
            0.0
        }*/
    }

    override fun command(target: Double) {
        isProducing = target != 0.0
    }
}