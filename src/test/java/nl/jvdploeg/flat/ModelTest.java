package nl.jvdploeg.flat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ModelTest {

  private static final Path PATH_A = new Path(new String[] { "A" });
  private static final Path PATH_A_A = new Path(new String[] { "A", "A" });
  private static final Path PATH_A_B = new Path(new String[] { "A", "B" });
  private static final Path PATH_A_C = new Path(new String[] { "A", "C" });
  private static final Path PATH_A_C_E = new Path(new String[] { "A", "C", "E" });
  private static final Path PATH_A_C_E_B = new Path(new String[] { "A", "C", "E", "B" });
  private static final Path PATH_A_D = new Path(new String[] { "A", "D" });
  private static final Path PATH_D_B = new Path(new String[] { "D", "B" });
  private static final Path PATH_EMPTY = new Path(new String[] {});

  private Model model;

  @Before
  public void before() {
    model = new Model(ModelTest.class.getSimpleName(), Enforcement.STRICT);

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
  }

  @Test
  public void testGetBestValue() {
    Assert.assertEquals(null,
        NodeValueUtils.getBestValue(model.getRoot(), PATH_EMPTY.createChildPath("B")));
    Assert.assertEquals("b",
        NodeValueUtils.getBestValue(model.getRoot(), PATH_A.createChildPath("B")));
    Assert.assertEquals("b",
        NodeValueUtils.getBestValue(model.getRoot(), PATH_A_A.createChildPath("B")));
    Assert.assertEquals("b",
        NodeValueUtils.getBestValue(model.getRoot(), PATH_A_B.createChildPath("B")));
    Assert.assertEquals("bb",
        NodeValueUtils.getBestValue(model.getRoot(), PATH_A_C_E.createChildPath("B")));
    Assert.assertEquals("bb",
        NodeValueUtils.getBestValue(model.getRoot(), PATH_A_C_E_B.createChildPath("B")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemove_ChildDoesNotExist() {
    model.remove(PATH_A_D);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRemove_ParentDoesNotExist() {
    model.remove(PATH_D_B);
  }

  @Test
  public void testSunnyDay() {
    final Node root = model.getRoot();
    Assert.assertEquals("a", NodeUtils.getNode(root, PATH_A).getValue());
    Assert.assertEquals("b", NodeUtils.getNode(root, PATH_A_B).getValue());
    Assert.assertEquals("c", NodeUtils.getNode(root, PATH_A_C).getValue());
  }
}
