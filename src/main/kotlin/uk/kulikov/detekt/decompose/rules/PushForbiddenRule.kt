package uk.kulikov.detekt.decompose.rules

import dev.detekt.api.Config
import dev.detekt.api.Entity
import dev.detekt.api.Finding
import dev.detekt.api.RequiresAnalysisApi
import dev.detekt.api.Rule
import dev.detekt.psi.isCalling
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtImportDirective
import org.jetbrains.kotlin.resolve.ImportPath

class PushForbiddenRule(config: Config) : Rule(
    config,
    getDescription(config),
), RequiresAnalysisApi {
    private val checkImport = config.valueOrDefault("checkImport", true)

    override fun visitImportDirective(importDirective: KtImportDirective) {
        super.visitImportDirective(importDirective)

        if (!checkImport) {
            return
        }

        if (importDirective.importPath == importPath) {
            report(Finding(Entity.from(importDirective), description))
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)

        if (expression.isCalling(requireFunctionCallableId)) {
            report(Finding(Entity.from(expression), description))
        }
    }

    companion object {
        private val requireFunctionCallableId = CallableId(
            FqName("com.arkivanov.decompose.router.stack"),
            Name.identifier("push"),
        )
        private val importPath = ImportPath.fromString("com.arkivanov.decompose.router.stack.push")

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
    }
}
