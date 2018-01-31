// The author disclaims copyright to this source code.
package nl.jvdploeg.flat.util;

import nl.jvdploeg.flat.Path;
import nl.jvdploeg.flat.impl.DefaultModel;
import nl.jvdploeg.flat.impl.Enforce;

public abstract class TestModel {

  public static final Path PATH_A = new Path("A");

  public static final Path PATH_A_A = new Path("A", "A");
  public static final Path PATH_A_B = new Path("A", "B");
  public static final Path PATH_A_C = new Path("A", "C");
  public static final Path PATH_A_C_E = new Path("A", "C", "E");
  public static final Path PATH_A_C_E_B = new Path("A", "C", "E", "B");
  public static final Path PATH_A_D = new Path("A", "D");
  public static final Path PATH_D_B = new Path("D", "B");
  public static final Path PATH_EMPTY = new Path();

  public static DefaultModel create() {
    final DefaultModel model = new DefaultModel(TestModel.class.getSimpleName(), Enforce.STRICT);

    model.add(PATH_A);
    model.setValue(PATH_A, "a");
    model.add(PATH_A_A);
    model.setValue(PATH_A_A, "aa");
    model.add(PATH_A_B);
    model.setValue(PATH_A_B, "b");
    model.add(PATH_A_C);
    model.setValue(PATH_A_C, "c");
    model.add(PATH_A_C_E);
    model.add(PATH_A_C_E_B);
    model.setValue(PATH_A_C_E_B, "bb");
    model.add(PATH_A_D);
    model.setValue(PATH_A_D, "d");
    model.remove(PATH_A_D);
    return model;
  }

  private TestModel() {
  }
}
