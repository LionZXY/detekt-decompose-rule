package uk.kulikov.detekt.decompose

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider
import uk.kulikov.detekt.decompose.rules.PushForbiddenRule

class DetektDecomposeSetProvider : RuleSetProvider {
    override val ruleSetId = RuleSetId("DecomposeRule")

    override fun instance(): RuleSet {
        return RuleSet(ruleSetId, listOf(::PushForbiddenRule))
    }
}
