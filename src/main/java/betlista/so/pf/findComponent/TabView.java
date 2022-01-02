package betlista.so.pf.findComponent;

import com.sun.faces.component.visit.FullVisitContext;
import org.primefaces.PrimeFaces;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import java.util.LinkedList;
import java.util.List;

@Component
public class TabView {

    List<TabData> tabData = new LinkedList<>();

    {
        tabData.add(new TabData("name 1", "val1-a", null));
        tabData.add(new TabData("name b", "val1-b", "val2-b"));
    }

    public List<TabData> getTabsData() {
        return tabData;
    }

    public void save() {
        boolean isValid = isValid();
        if (isValid) {
            // continue ...
        }
    }

    private boolean isValid() {
        boolean isOk = isOk();
        if (isOk) {
            return true;
        }
		FacesMessage message = new FacesMessage("Not saved!");
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, message);

		final UIViewRoot viewRoot = context.getViewRoot();
		List<UIInput> componentList = new LinkedList<>();
		viewRoot.visitTree(new FullVisitContext(context), new VisitCallback() {
			@Override
			public VisitResult visit(VisitContext context, UIComponent target) {
				if (target != null) {
                    final String id = target.getId();
                    if ("val1".equals(id) || "val2".equals(id)) {
                        if (target instanceof UIInput) {
                            componentList.add((UIInput) target);
                        }
                    }
				}
				return VisitResult.ACCEPT;
			}
		});
		for (UIInput uiInput: componentList) {
			uiInput.setValid(false);
		}
		context.validationFailed();
        PrimeFaces.current().ajax().update("form");
        final UIComponent val1 = context.getViewRoot().findComponent("val1");
        return false;
    }

    private boolean isOk() {
        return false;
    }

}
