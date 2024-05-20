package org.pmsys.main.actions.auth;

import org.pmsys.main.entities.request.AuthRequestStatus;
import org.pmsys.main.actions.SimpleAction;
import org.pmsys.main.entities.request.AuthRequest;
import org.pmsys.main.entities.result.AuthResult;
import org.pmsys.main.managers.ViewManager;
import org.pmsys.main.service.AuthService;
import org.pmsys.main.managers.ServiceManager;
import org.pmsys.main.service.Services;
import org.pmsys.main.ui.auth.AbstractAuthPanel;
import org.pmsys.main.ui.auth.AuthWindow;
import org.pmsys.main.ui.CComponent;
import org.pmsys.main.ui.views.Views;

import javax.swing.*;


public abstract class AbstractAuthAction implements SimpleAction {

    protected final AuthService authService;

    protected AbstractAuthAction() {
        authService = (AuthService) ServiceManager.getService(Services.AUTHENTICATION);
    }

    protected abstract AuthResult executeAction(AuthRequest request);

    protected abstract void handleSuccess(CComponent view, AuthResult authResult);

    protected abstract void handleFailure(CComponent view, AuthResult authResult);
    
    @Override
    public final void execute(JComponent source, CComponent view) {
        AbstractAuthPanel authForm = (AbstractAuthPanel) view;
        AuthWindow authWindow = authForm.getAuthWindow();

        AuthRequest request = authForm.getAuthRequest();

        if (request == null) {
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                authWindow.toggleLoadingState(true);
                AuthResult authResult = executeAction(request);

                if (authResult.getStatus() == AuthRequestStatus.SUCCESS) {
                    handleSuccess(view, authResult);
                } else {
                    handleFailure(view, authResult);
                }

                return null;
            }

            @Override
            protected void done() {
                authWindow.toggleLoadingState(false);
                ViewManager.INSTANCE.showView(Views.PROJECT_LIST);
            }
        }.execute();
    }
}