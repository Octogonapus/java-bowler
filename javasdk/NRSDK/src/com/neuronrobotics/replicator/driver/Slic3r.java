package com.neuronrobotics.replicator.driver;

import java.io.File;
import java.util.Arrays;

public class Slic3r extends ExternalSlicer {
	
	

	public Slic3r(	File executable,
					double nozzle_diameter,
					double [] printCenter,
					double filimentDiameter,
					double extrusionMultiplier,
					int tempreture,
					int bedTempreture,
					double layerHeight,
					int wallThickness,
					boolean useSupportMaterial,
					double retractLength
					) {
		if(!executable.canExecute())
			throw new RuntimeException("Slicer binary must be executable.");
		String exe = executable.getAbsolutePath();
		this.cmdline=Arrays.asList(exe,
				"--nozzle-diameter="+nozzle_diameter,
				"--print-center=("+printCenter[0]+","+printCenter[0]+")",
				"--filament-diameter="+filimentDiameter,
				"--extrusion-multiplier="+extrusionMultiplier,
				"--temperature="+tempreture,
				"--bed-temperature="+bedTempreture,
				"--layer-height="+layerHeight,
				"--perimeters="+wallThickness,
				"--avoid-crossing-perimeters",
				useSupportMaterial?"--support-material":" ",
				"--retract-length="+retractLength,
				"--skirts=2",
				"--notes=\"Generated by com.neuronrobotics.replicator.driver.Slic3r.java\""
			);
	}
	
//	public static void main(String args[]) throws Exception {
//		ExternalSlicer slicer=new Slic3r(new MiracleGrueMaterialData());
//		FileInputStream stlFile=new FileInputStream(args[0]);
//		FileOutputStream dumpFile=new FileOutputStream(args[0]+"-dump.gcode");
//		slicer.slice(stlFile, dumpFile);
//	}

}
