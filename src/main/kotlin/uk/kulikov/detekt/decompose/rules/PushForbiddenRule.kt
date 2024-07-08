package uk.kulikov.detekt.decompose.rules

import io.gitlab.arturbosch.detekt.api.*
import io.gitlab.arturbosch.detekt.rules.isCalling
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.resolve.ImportPath

class PushForbiddenRule(config: Config) : Rule(
    config
) {
    private val description = getDescription(config)
    override val issue = Issue(
        "PushForbiddenRule",
        Severity.CodeSmell,
        description,
        Debt.FIVE_MINS,
    )
    private val checkImport = config.valueOrDefault("checkImport", true)

    override fun visitImportDirective(importDirective: KtImportDirective) {
        super.visitImportDirective(importDirective)

        if (!checkImport) {
            return
        }

        if (importDirective.importPath == importPath) {
            report(CodeSmell(issue, Entity.from(importDirective), issue.description))
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (expression.isCalling(requireFunctionFqName, bindingContext)) {
            report(CodeSmell(issue, Entity.from(expression), issue.description))
        }
    }

    private fun getDescription(config: Config): String {
        var replaceTo = config.valueOrNull<String>("replaceTo")
        if (replaceTo == null) {
            replaceTo = config.subConfig("PushForbiddenRule")
                .valueOrNull("replaceTo")
        }
        return if (replaceTo == null) {
            "The push() method can cause crashes in runtime. Use safer ways to add a screen to the stack. More information: https://arkivanov.github.io/Decompose/navigation/stack/navigation/#stacknavigator-extension-functions"
        } else {
            "Use $replaceTo instead of push() to avoid runtime crashes"
        }
    }

    companion object {
        private val requireFunctionFqName = FqName("com.arkivanov.decompose.router.stack.push")
        private val importPath = ImportPath.fromString("com.arkivanov.decompose.router.stack.push")
    }
}
