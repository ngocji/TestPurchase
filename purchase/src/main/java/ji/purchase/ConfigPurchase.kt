package ji.purchase

data class ConfigPurchase(
    val nonConsumeProducts: List<String> = emptyList(),
    val consumeProducts: List<String> = emptyList(),
    val subscriptionProducts: List<String> = emptyList(),
    val licenseKey: String
)