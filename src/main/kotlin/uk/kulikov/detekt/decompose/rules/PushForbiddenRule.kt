package uk.kulikov.detekt.decompose.rules

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.isCalling
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallExpression

class PushForbiddenRule(config: Config) : Rule(
    config
) {
    override val issue = Issue(
        "PushForbiddenRule",
        Severity.CodeSmell,
        "Use pushToFront() instead of push() to avoid runtime crashes",
        Debt.FIVE_MINS,
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (expression.isCalling(requireFunctionFqName, bindingContext)) {
            report(CodeSmell(issue, Entity.from(expression), issue.description))
        }
    }

    companion object {
        private val requireFunctionFqName = FqName("com.arkivanov.decompose.router.stack.push")
    }
}
