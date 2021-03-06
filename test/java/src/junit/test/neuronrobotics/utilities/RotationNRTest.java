package junit.test.neuronrobotics.utilities;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;

import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.RotationOrder;
import org.junit.Test;

import com.neuronrobotics.sdk.addons.kinematics.DHParameterKinematics;
import com.neuronrobotics.sdk.addons.kinematics.MobileBase;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNRLegacy;
import com.neuronrobotics.sdk.addons.kinematics.math.RotationNR;
import com.neuronrobotics.sdk.addons.kinematics.math.TransformNR;
import com.neuronrobotics.sdk.addons.kinematics.parallel.ParallelGroup;
import com.neuronrobotics.sdk.common.Log;
import com.neuronrobotics.sdk.util.ThreadUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class RotationNRTest.
 */
public class RotationNRTest {

	private RotationOrder[] list = new RotationOrder[] { RotationOrder.ZYX
			// RotationOrder.XZY,
			// RotationOrder.YXZ,
			// RotationOrder.YZX,
			// RotationOrder.ZXY, RotationOrder.ZYX, RotationOrder.XYX,
			// RotationOrder.XZX, RotationOrder.YXY,
			// RotationOrder.YZY, RotationOrder.ZXZ, RotationOrder.ZYZ

	};;

	RotationConvention[] conventions = { RotationConvention.VECTOR_OPERATOR };

	/**
	 * Test.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void test() throws FileNotFoundException {
		int failCount = 0;
		int iterations = 100;

		for (RotationConvention conv : conventions) {
			RotationNR.setConvention(conv);
			System.out.println("\n\nUsing convention " + conv.toString());
			for (RotationOrder ro : list) {
				RotationNR.setOrder(ro);
				System.out.println("\n\nUsing rotationOrder " + ro.toString());
				//
				for (int i = 0; i < iterations; i++) {

					double tilt = Math.toRadians((Math.random() * 360) - 180);
					double elevation = Math.toRadians((Math.random() * 180) - 90);
					double azumus = Math.toRadians((Math.random() * 360) - 180);
					try {
						RotationNR rotTest = new RotationNR(Math.toDegrees(tilt), Math.toDegrees(azumus),
								Math.toDegrees(elevation));
						System.out.println("\n\nTest #" + i);
						System.out.println("Testing Az=" + Math.toDegrees(azumus) + " El=" + Math.toDegrees(elevation)
								+ " Tl=" + Math.toDegrees(tilt));
						System.out.println("Got     Az=" + Math.toDegrees(rotTest.getRotationAzimuth()) + " El="
								+ Math.toDegrees(rotTest.getRotationElevation()) + " Tl="
								+ Math.toDegrees(rotTest.getRotationTilt()));

						if (!RotationNR.bound(tilt - .01, tilt + .01, rotTest.getRotationTilt())) {
							failCount++;
							System.err.println("Rotation Tilt is not consistant. expected " + Math.toDegrees(tilt)
									+ " got " + Math.toDegrees(rotTest.getRotationTilt()) + " \t\tOff By "
									+ (Math.toDegrees(tilt) - Math.toDegrees(rotTest.getRotationTilt())));
						}
						if (!RotationNR.bound(elevation - .01, elevation + .01, rotTest.getRotationElevation())) {
							failCount++;
							System.err.println("Rotation Elevation is not consistant. expected "
									+ Math.toDegrees(elevation) + " got "
									+ Math.toDegrees(rotTest.getRotationElevation()) + " \t\tOff By "
									+ (Math.toDegrees(elevation) + Math.toDegrees(rotTest.getRotationElevation()))

							);
						}
						if (!RotationNR.bound(azumus - .01, azumus + .01, rotTest.getRotationAzimuth())) {
							failCount++;
							System.err.println("Rotation azumus is not consistant. expected " + Math.toDegrees(azumus)
									+ " got " + Math.toDegrees(rotTest.getRotationAzimuth()) + " \t\tOff By "
									+ (Math.toDegrees(azumus) - Math.toDegrees(rotTest.getRotationAzimuth())));
						}
						ThreadUtil.wait(20);
					} catch (NumberFormatException ex) {
						if (elevation >= Math.PI / 2 || elevation <= -Math.PI / 2) {
							System.out.println("Invalid numbers rejected ok");
						}
					}

				}

				// frame();
				// frame2();
				System.out.println("Frame test passed with " + ro);
				// return;
			}
		}
		if (failCount > 1) {
			fail();

		}
	}

	/**
	 * Test.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void compareAzemuth() throws FileNotFoundException {
		int failCount = 0;
		int iterations = 100;
		Log.enableDebugPrint();
		for (RotationConvention conv : conventions) {
			RotationNR.setConvention(conv);
			System.out.println("\n\nUsing convention " + conv.toString());
			for (RotationOrder ro : list) {
				RotationNR.setOrder(ro);
				System.out.println("\n\nUsing rotationOrder " + ro.toString());
				failCount = 0;
				for (int i = 0; i < iterations; i++) {

					double rotationAngleDegrees = (Math.random() * 360) - 180;

					double rotationAngleRadians = Math.PI / 180 * rotationAngleDegrees;

					double[][] rotation = new double[3][3];
					// Rotation matrix, 1st column
					rotation[0][0] = Math.cos(rotationAngleRadians);
					rotation[1][0] = Math.sin(rotationAngleRadians);
					rotation[2][0] = 0;
					// Rotation matrix, 2nd column
					rotation[0][1] = -Math.sin(rotationAngleRadians);
					rotation[1][1] = Math.cos(rotationAngleRadians);
					rotation[2][1] = 0;
					// Rotation matrix, 3rd column
					rotation[0][2] = 0;
					rotation[1][2] = 0;
					rotation[2][2] = 1;
					// pure rotation in azumuth
					RotationNR newRot = new RotationNR(rotation);
					RotationNRLegacy oldRot = new RotationNRLegacy(rotation);
					double[][] rotationMatrix = newRot.getRotationMatrix();
					System.out.println("Testing pure azumeth \nrotation " + rotationAngleDegrees + "\n as radian "
							+ Math.toRadians(rotationAngleDegrees) + "\n     Az " + oldRot.getRotationAzimuth()
							+ "\n     El " + oldRot.getRotationElevation() + "\n     Tl " + oldRot.getRotationTilt()
							+ "\n New Az " + newRot.getRotationAzimuth() + "\n New El " + newRot.getRotationElevation()
							+ "\n New Tl " + newRot.getRotationTilt());
					assertArrayEquals(rotation[0], rotationMatrix[0], 0.001);
					assertArrayEquals(rotation[1], rotationMatrix[1], 0.001);
					assertArrayEquals(rotation[2], rotationMatrix[2], 0.001);

					System.out.println(
							"Testing Quaturnion \nrotation " + "\n     qw " + oldRot.getRotationMatrix2QuaturnionW()
									+ "\n     qx " + oldRot.getRotationMatrix2QuaturnionX() + "\n     qy "
									+ oldRot.getRotationMatrix2QuaturnionY() + "\n     qz "
									+ oldRot.getRotationMatrix2QuaturnionZ() + "\nNEW  qw "
									+ newRot.getRotationMatrix2QuaturnionW() + "\nNEW  qx "
									+ newRot.getRotationMatrix2QuaturnionX() + "\nNEW  qy "
									+ newRot.getRotationMatrix2QuaturnionY() + "\nNEW  qz "
									+ newRot.getRotationMatrix2QuaturnionZ());
					assertArrayEquals(
							new double[] { Math.abs(oldRot.getRotationMatrix2QuaturnionW()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionX()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionY()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionZ()), },
							new double[] { Math.abs(newRot.getRotationMatrix2QuaturnionW()),
									Math.abs(newRot.getRotationMatrix2QuaturnionX()),
									Math.abs(newRot.getRotationMatrix2QuaturnionY()),
									Math.abs(newRot.getRotationMatrix2QuaturnionZ()), },
							0.001);
					// Check Euler angles
					// this check is needed to work around a known bug in the
					// legact implementation
					if (!(rotationAngleDegrees >= 90 || rotationAngleDegrees <= -90)) {
						assertArrayEquals(
								new double[] { oldRot.getRotationAzimuth(), oldRot.getRotationElevation(),
										oldRot.getRotationTilt() },
								new double[] { newRot.getRotationAzimuth(), newRot.getRotationElevation(),
										newRot.getRotationTilt() },
								0.001);
						// Check the old rotation against the known value
						assertArrayEquals(new double[] { Math.toRadians(rotationAngleDegrees), 0, 0 }, new double[] {
								oldRot.getRotationAzimuth(), oldRot.getRotationElevation(), oldRot.getRotationTilt() },
								0.001);
					} else {
						System.err.println("Legacy angle would fail here " + rotationAngleDegrees);
					}
					// Check the new rotation against the known value
					assertArrayEquals(new double[] { Math.toRadians(rotationAngleDegrees), 0, 0 }, new double[] {
							newRot.getRotationAzimuth(), newRot.getRotationElevation(), newRot.getRotationTilt() },
							0.001);
				}
				// frame();
				// frame2();
				System.out.println("Frame test passed with " + ro);
				// return;
			}
		}
	}

	/**
	 * Test.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void compareElevation() throws FileNotFoundException {
		int failCount = 0;
		int iterations = 100;
		for (RotationConvention conv : conventions) {
			RotationNR.setConvention(conv);
			System.out.println("\n\nUsing convention " + conv.toString());
			for (RotationOrder ro : list) {
				RotationNR.setOrder(ro);
				System.out.println("\n\nUsing rotationOrder " + ro.toString());
				failCount = 0;
				for (int i = 0; i < iterations; i++) {

					double rotationAngleDegrees = (Math.random() * 180) - 90;

					double rotationAngleRadians = Math.PI / 180 * rotationAngleDegrees;

					double[][] rotation = new double[3][3];
					// Rotation matrix, 1st column
					rotation[0][0] = Math.cos(rotationAngleRadians);
					rotation[1][0] = 0;
					rotation[2][0] = -Math.sin(rotationAngleRadians);
					// Rotation matrix, 2nd column
					rotation[0][1] = 0;
					rotation[1][1] = 1;
					rotation[2][1] = 0;
					// Rotation matrix, 3rd column
					rotation[0][2] = Math.sin(rotationAngleRadians);
					rotation[1][2] = 0;
					rotation[2][2] = Math.cos(rotationAngleRadians);
					// pure rotation in azumuth
					RotationNR newRot = new RotationNR(rotation);
					RotationNRLegacy oldRot = new RotationNRLegacy(rotation);
					double[][] rotationMatrix = newRot.getRotationMatrix();
					System.out.println("Testing pure elevation \nrotation " + rotationAngleDegrees + "\n as radian "
							+ Math.toRadians(rotationAngleDegrees) + "\n     Az " + oldRot.getRotationAzimuth()
							+ "\n     El " + oldRot.getRotationElevation() + "\n     Tl " + oldRot.getRotationTilt()
							+ "\n New Az " + newRot.getRotationAzimuth() + "\n New El " + newRot.getRotationElevation()
							+ "\n New Tl " + newRot.getRotationTilt());
					assertArrayEquals(rotation[0], rotationMatrix[0], 0.001);
					assertArrayEquals(rotation[1], rotationMatrix[1], 0.001);
					assertArrayEquals(rotation[2], rotationMatrix[2], 0.001);

					System.out.println(
							"Testing Quaturnion \nrotation " + "\n     qw " + oldRot.getRotationMatrix2QuaturnionW()
									+ "\n     qx " + oldRot.getRotationMatrix2QuaturnionX() + "\n     qy "
									+ oldRot.getRotationMatrix2QuaturnionY() + "\n     qz "
									+ oldRot.getRotationMatrix2QuaturnionZ() + "\nNEW  qw "
									+ newRot.getRotationMatrix2QuaturnionW() + "\nNEW  qx "
									+ newRot.getRotationMatrix2QuaturnionX() + "\nNEW  qy "
									+ newRot.getRotationMatrix2QuaturnionY() + "\nNEW  qz "
									+ newRot.getRotationMatrix2QuaturnionZ());
					assertArrayEquals(
							new double[] { Math.abs(oldRot.getRotationMatrix2QuaturnionW()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionX()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionY()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionZ()), },
							new double[] { Math.abs(newRot.getRotationMatrix2QuaturnionW()),
									Math.abs(newRot.getRotationMatrix2QuaturnionX()),
									Math.abs(newRot.getRotationMatrix2QuaturnionY()),
									Math.abs(newRot.getRotationMatrix2QuaturnionZ()), },
							0.001);
					// Check Euler angles
					assertArrayEquals(
							new double[] { oldRot.getRotationAzimuth(), oldRot.getRotationElevation(),
									oldRot.getRotationTilt() },
							new double[] { newRot.getRotationAzimuth(), newRot.getRotationElevation(),
									newRot.getRotationTilt() },
							0.001);
					// Check the old rotation against the known value
					assertArrayEquals(new double[] {

							0, Math.toRadians(rotationAngleDegrees), 0 },
							new double[] { oldRot.getRotationAzimuth(), oldRot.getRotationElevation(),
									oldRot.getRotationTilt() },
							0.001);
					// Check the new rotation against the known value
					assertArrayEquals(new double[] { 0, Math.toRadians(rotationAngleDegrees), 0 }, new double[] {
							newRot.getRotationAzimuth(), newRot.getRotationElevation(), newRot.getRotationTilt() },
							0.001);
				}
				// frame();
				// frame2();
				System.out.println("Frame test passed with " + ro);
				// return;
			}
		}
	}

	/**
	 * Test.
	 * 
	 * @throws FileNotFoundException
	 */
	@Test
	public void compareTilt() throws FileNotFoundException {
		int failCount = 0;
		int iterations = 100;
		for (RotationConvention conv : conventions) {
			RotationNR.setConvention(conv);
			System.out.println("\n\nUsing convention " + conv.toString());
			for (RotationOrder ro : list) {
				RotationNR.setOrder(ro);
				System.out.println("\n\nUsing rotationOrder " + ro.toString());
				failCount = 0;
				for (int i = 0; i < iterations; i++) {

					double rotationAngleDegrees = (Math.random() * 360) - 180;

					double rotationAngleRadians = Math.PI / 180 * rotationAngleDegrees;

					double[][] rotation = new double[3][3];
					// Rotation matrix, 1st column
					rotation[0][0] = 1;
					rotation[1][0] = 0;
					rotation[2][0] = 0;
					// Rotation matrix, 2nd column
					rotation[0][1] = 0;
					rotation[1][1] = Math.cos(rotationAngleRadians);
					rotation[2][1] = Math.sin(rotationAngleRadians);
					// Rotation matrix, 3rd column
					rotation[0][2] = 0;
					rotation[1][2] = -Math.sin(rotationAngleRadians);
					rotation[2][2] = Math.cos(rotationAngleRadians);
					// pure rotation in azumuth
					RotationNR newRot = new RotationNR(rotation);
					RotationNRLegacy oldRot = new RotationNRLegacy(rotation);
					double[][] rotationMatrix = newRot.getRotationMatrix();
					System.out.println("Testing pure tilt \nrotation " + rotationAngleDegrees + "\n as radian "
							+ Math.toRadians(rotationAngleDegrees) + "\n     Az " + oldRot.getRotationAzimuth()
							+ "\n     El " + oldRot.getRotationElevation() + "\n     Tl " + oldRot.getRotationTilt()
							+ "\n New Az " + newRot.getRotationAzimuth() + "\n New El " + newRot.getRotationElevation()
							+ "\n New Tl " + newRot.getRotationTilt());
					assertArrayEquals(rotation[0], rotationMatrix[0], 0.001);
					assertArrayEquals(rotation[1], rotationMatrix[1], 0.001);
					assertArrayEquals(rotation[2], rotationMatrix[2], 0.001);

					System.out.println(
							"Testing Quaturnion \nrotation " + "\n     qw " + oldRot.getRotationMatrix2QuaturnionW()
									+ "\n     qx " + oldRot.getRotationMatrix2QuaturnionX() + "\n     qy "
									+ oldRot.getRotationMatrix2QuaturnionY() + "\n     qz "
									+ oldRot.getRotationMatrix2QuaturnionZ() + "\nNEW  qw "
									+ newRot.getRotationMatrix2QuaturnionW() + "\nNEW  qx "
									+ newRot.getRotationMatrix2QuaturnionX() + "\nNEW  qy "
									+ newRot.getRotationMatrix2QuaturnionY() + "\nNEW  qz "
									+ newRot.getRotationMatrix2QuaturnionZ());
					assertArrayEquals(
							new double[] { Math.abs(oldRot.getRotationMatrix2QuaturnionW()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionX()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionY()),
									Math.abs(oldRot.getRotationMatrix2QuaturnionZ()), },
							new double[] { Math.abs(newRot.getRotationMatrix2QuaturnionW()),
									Math.abs(newRot.getRotationMatrix2QuaturnionX()),
									Math.abs(newRot.getRotationMatrix2QuaturnionY()),
									Math.abs(newRot.getRotationMatrix2QuaturnionZ()), },
							0.001);
					// Check Euler angles
					assertArrayEquals(
							new double[] { oldRot.getRotationAzimuth(), oldRot.getRotationElevation(),
									oldRot.getRotationTilt() },
							new double[] { newRot.getRotationAzimuth(), newRot.getRotationElevation(),
									newRot.getRotationTilt() },
							0.001);
					// Check the old rotation against the known value
					assertArrayEquals(new double[] { 0, 0, Math.toRadians(rotationAngleDegrees) }, new double[] {
							oldRot.getRotationAzimuth(), oldRot.getRotationElevation(), oldRot.getRotationTilt() },
							0.001);
					// Check the new rotation against the known value
					assertArrayEquals(new double[] { 0, 0, Math.toRadians(rotationAngleDegrees) }, new double[] {
							newRot.getRotationAzimuth(), newRot.getRotationElevation(), newRot.getRotationTilt() },
							0.001);
				}
				// frame();
				// frame2();
				System.out.println("Frame test passed with " + ro);
				// return;
			}
		}
	}

	@Test
	public void checkEulerSingularities() {
		RotationNR tester1 = new RotationNR(0.7071067811865476, 0, 0.7071067811865475, 0);
		RotationNR tester2 = new RotationNR(4.329780281177467E-17, -0.7071067811865475, 4.329780281177466E-17,
				0.7071067811865476);
		RotationNR tester3 = new RotationNR(0.7064894449532356, 1.0769850738285257E-7, 0.7077235789272859,
				1.0769850738285257E-7);
		assertArrayEquals(new double[] { 0, 90, 0 }, new double[] { Math.toDegrees(tester1.getRotationAzimuth()),
				Math.toDegrees(tester1.getRotationElevation()), Math.toDegrees(tester1.getRotationTilt()) },

				0.001);
		assertArrayEquals(new double[] { 0, 90, 180 }, new double[] { Math.toDegrees(tester2.getRotationAzimuth()),
				Math.toDegrees(tester2.getRotationElevation()), Math.toDegrees(tester2.getRotationTilt()) },

				0.001);
		assertArrayEquals(new double[] { 179.99, 89.9, 179.99 }, new double[] { Math.toDegrees(tester3.getRotationAzimuth()),
				Math.toDegrees(tester3.getRotationElevation()), Math.toDegrees(tester3.getRotationTilt()) },

				0.001);
	}

}
