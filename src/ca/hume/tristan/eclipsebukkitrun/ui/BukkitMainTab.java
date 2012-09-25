package ca.hume.tristan.eclipsebukkitrun.ui;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import ca.hume.tristan.eclipsebukkitrun.BukkitLaunchConfigurationConstants;

public class BukkitMainTab extends AbstractLaunchConfigurationTab {

	protected FolderChooser chooser;

	private class WidgetListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			scheduleUpdateJob();
		}
	}
	private WidgetListener fListener = new WidgetListener();

	@Override
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		comp.setLayout(layout);
		comp.setFont(font);

		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);
		setControl(comp);

		Group group = new Group(comp, SWT.NONE);
		group.setFont(font);
		layout = new GridLayout();
		group.setLayout(layout);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText("Server Folder Location");

		chooser = new FolderChooser(group);
		chooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		chooser.setTitle("Select Bukkit Server Folder");
		chooser.getTextControl().addModifyListener(fListener);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BukkitLaunchConfigurationConstants.ATTR_SERVER_DIR, "");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			chooser.getTextControl().setText(configuration.getAttribute(
					BukkitLaunchConfigurationConstants.ATTR_SERVER_DIR, ""));
		} catch(CoreException ce) {
			// none set, leave it blank
		}

	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(BukkitLaunchConfigurationConstants.ATTR_SERVER_DIR, chooser.getText());

	}

	public boolean isValid(ILaunchConfiguration configuration) {
		setErrorMessage(null);
		if(!new File(chooser.getText()).isDirectory()) {
			setErrorMessage("Server directory does not exist.");
			return false;
		}
		return true;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bukkit Server";
	}

	@Override
	public String getId() {
		return "ca.hume.tristan.eclipsebukkitrun.ui.bukkitMainTab";
	}

}
