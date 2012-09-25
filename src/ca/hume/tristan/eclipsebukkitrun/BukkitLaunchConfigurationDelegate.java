package ca.hume.tristan.eclipsebukkitrun;

import java.io.File;
import java.io.FilenameFilter;
import java.text.MessageFormat;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class BukkitLaunchConfigurationDelegate extends org.eclipse.jdt.launching.JavaLaunchDelegate {
	@Override
	public String verifyMainTypeName(ILaunchConfiguration configuration) throws CoreException {
		return "org.bukkit.craftbukkit.Main";
	}
	@Override
	public String[] getClasspath(ILaunchConfiguration configuration) throws CoreException {
		String[] classpath = new String[1];
		File workingDir = verifyWorkingDirectory(configuration);
		File[] bukkitJars = workingDir.listFiles(new FilenameFilter() { 
			public boolean accept(File dir, String filename){
				return filename.contains("bukkit") && filename.endsWith(".jar");
			}
		});
		if(bukkitJars.length > 1) {
			throw new CoreException(new Status(
					IStatus.ERROR,"ca.hume.tristan.EclipseBukkitRun",
					"More than one bukkit jar in server directory."));
		}
		if(bukkitJars.length < 1) {
			throw new CoreException(new Status(
					IStatus.ERROR,"ca.hume.tristan.EclipseBukkitRun",
					"No bukkit jar in server directory."));
		}
		classpath[0] = bukkitJars[0].getAbsolutePath();
		return classpath;
	}
	@Override
	public String getVMArguments(ILaunchConfiguration configuration) throws CoreException {
		String args = "-Djline.terminal=jline.UnsupportedTerminal";
		// add the java library path argument if necessary, copied from the AbstractJavaLaunchConfigurationDelegate class
		String[] javaLibraryPath = getJavaLibraryPath(configuration);
		if (javaLibraryPath != null && javaLibraryPath.length > 0) {
			StringBuffer path = new StringBuffer(args);
			path.append(" -Djava.library.path="); //$NON-NLS-1$
			path.append("\""); //$NON-NLS-1$
			for (int i = 0; i < javaLibraryPath.length; i++) {
				if (i > 0) {
					path.append(File.pathSeparatorChar);
				}
				path.append(javaLibraryPath[i]);
			}
			path.append("\""); //$NON-NLS-1$
			args = path.toString();
		}
		return args;
	}
	public File verifyWorkingDirectory(ILaunchConfiguration configuration) throws CoreException {
		String path = configuration.getAttribute(BukkitLaunchConfigurationConstants.ATTR_SERVER_DIR, "");
		File dir = new File(path);
		if (dir.isDirectory()) {
			return dir;
		}
		// otherwise error
		abort(
		 	MessageFormat
		 	.format("The bukkit server directory {1} was not found.",
		 	(Object[])new String[]{path.toString()}),
		 	null,
		 	IJavaLaunchConfigurationConstants.ERR_WORKING_DIRECTORY_DOES_NOT_EXIST); 
		return null;
	}
}
