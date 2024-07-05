package uk.kulikov.detekt.decompose

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider
import uk.kulikov.detekt.decompose.rules.PushForbiddenRule

class DetektDecomposeSetProvider : RuleSetProvider {
    override val ruleSetId: String = "DecomposeRule"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                PushForbiddenRule(config),
            ),
        )
    }
}
