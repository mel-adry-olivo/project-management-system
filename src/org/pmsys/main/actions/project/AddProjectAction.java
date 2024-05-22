package org.pmsys.main.actions.project;

import org.pmsys.main.entities.Project;
import org.pmsys.main.entities.request.ProjectRequest;
import org.pmsys.main.entities.request.ProjectRequestStatus;
import org.pmsys.main.entities.result.ProjectResult;
import org.pmsys.main.managers.IndexingManager;
import org.pmsys.main.ui.CComponent;

import javax.swing.*;

public class AddProjectAction extends AbstractProjectAction {

    @Override
    public void execute(JComponent source, CComponent comp) {
        ProjectRequest projectRequest = (ProjectRequest) projectForm.getFormData();
        ProjectResult result = projectService.validateProjectRequest(projectRequest);

        if(result.getStatus() != ProjectRequestStatus.SUCCESS) {
            handleProjectFormError(result);
            return;
        }

        Project validatedProject = result.getProject();
        projectService.saveProject(validatedProject);
        projectListView.addProjectToUI(projectListView.createProjectCard(validatedProject));
        IndexingManager.INSTANCE.indexProject(validatedProject);
        projectForm.dispose();
    }
}
