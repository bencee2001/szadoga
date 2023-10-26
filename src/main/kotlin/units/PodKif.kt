package units

class PodKif(
    podKifId: Int,
    private var isProducing: Boolean,
    private val maxPowerOut: Float
): AbstractUnit(podKifId, UnitType.KIF_POD) {

    override fun read(): Float {
        return if (isProducing){
            maxPowerOut
        } else {
            0F
        }
    }

    override fun command(target: Float) {
        isProducing = target != 0F
    }
}