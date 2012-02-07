package org.jetbrains.jet.plugin.liveTemplates.macro;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.*;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jet.lang.descriptors.ClassDescriptor;
import org.jetbrains.jet.lang.descriptors.ClassKind;
import org.jetbrains.jet.lang.descriptors.DeclarationDescriptor;
import org.jetbrains.jet.lang.psi.JetExpression;
import org.jetbrains.jet.lang.psi.JetFile;
import org.jetbrains.jet.lang.resolve.BindingContext;
import org.jetbrains.jet.lang.resolve.scopes.JetScope;
import org.jetbrains.jet.plugin.JetBundle;
import org.jetbrains.jet.plugin.compiler.WholeProjectAnalyzerFacade;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Evgeny Gerashchenko
 * @since 2/2/12
 */
public class JetAnonymousSuperMacro extends Macro {
    @Override
    public String getName() {
        return "anonymousSuper";
    }

    @Override
    public String getPresentableName() {
        return JetBundle.message("macro.fun.anonymousSuper");
    }

    @Override
    public Result calculateResult(@NotNull Expression[] params, final ExpressionContext context) {
        AnonymousTemplateEditingListener.registerListener(context.getEditor(), context.getProject());

        PsiNamedElement[] vars = getSupertypes(params, context);
        if (vars == null || vars.length == 0) return null;
        return new JetPsiElementResult(vars[0]);
    }

    @Override
    public LookupElement[] calculateLookupItems(@NotNull Expression[] params, ExpressionContext context) {
        final PsiNamedElement[] vars = getSupertypes(params, context);
        if (vars == null || vars.length < 2) return null;
        final Set<LookupElement> set = new LinkedHashSet<LookupElement>();
        for (PsiNamedElement var : vars) {
            set.add(LookupElementBuilder.create(var));
        }
        return set.toArray(new LookupElement[set.size()]);
    }

    @Nullable
    private static PsiNamedElement[] getSupertypes(Expression[] params, ExpressionContext context) {
        if (params.length != 0) return null;

        Project project = context.getProject();
        PsiDocumentManager.getInstance(project).commitAllDocuments();

        PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(context.getEditor().getDocument());
        if (!(psiFile instanceof JetFile)) return null;

        BindingContext bc = WholeProjectAnalyzerFacade.analyzeProjectWithCacheOnAFile((JetFile) psiFile);
        JetExpression expression = PsiTreeUtil.getParentOfType(psiFile.findElementAt(context.getStartOffset()), JetExpression.class);
        JetScope scope = bc.get(BindingContext.RESOLUTION_SCOPE, expression);
        if (scope == null) {
            return null;
        }

        List<PsiNamedElement> result = new ArrayList<PsiNamedElement>();

        for (DeclarationDescriptor descriptor : scope.getAllDescriptors()) {
            if (!(descriptor instanceof ClassDescriptor)) continue;
            ClassDescriptor classDescriptor = (ClassDescriptor) descriptor;
            if (!classDescriptor.getModality().isOverridable()) continue;
            ClassKind kind = classDescriptor.getKind();
            if (kind == ClassKind.TRAIT || kind == ClassKind.CLASS) {
                PsiElement declaration = bc.get(BindingContext.DESCRIPTOR_TO_DECLARATION, descriptor);
                if (declaration != null) {
                    result.add((PsiNamedElement) declaration);
                }
            }
        }

        return result.toArray(new PsiNamedElement[result.size()]);
    }
}
