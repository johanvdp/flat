// The author disclaims copyright to this source code.
package nl.jvdploeg.flat;

import org.junit.Assert;
import org.junit.Test;

public class PathTest {

  private static final Path A = new Path("A");
  private static final Path A2 = new Path("A");
  private static final Path B = new Path("B");
  private static final Path A_B = new Path("A", "B");

  @Test(expected = IllegalArgumentException.class)
  @SuppressWarnings("unused")
  public void testConstructor_DoesNotAllowNull() {
    new Path("A", null);
  }

  @Test
  public void testCreateChildPath_Path() {
    Assert.assertEquals(new Path("A", "B", "C", "D"), A_B.createChildPath(new Path("C", "D")));
  }

  @Test
  public void testCreateChildPath_Path_Empty() {
    Assert.assertEquals(new Path("A", "B"), A_B.createChildPath(new Path()));
  }

  @Test
  public void testCreateChildPath_String() {
    Assert.assertEquals(new Path("A", "B", "C"), A_B.createChildPath("C"));
  }

  @Test
  public void testCreateParentPath() {
    Assert.assertEquals(new Path("A"), A_B.createParentPath());
  }

  @Test
  public void testEqual_OtherNull() {
    Assert.assertFalse(A.equals(null));
  }

  @Test
  @SuppressWarnings("unlikely-arg-type")
  public void testEqual_OtherType() {
    Assert.assertFalse(A.equals("different class"));
  }

  @Test
  public void testGetLastNodeName() {
    Assert.assertEquals("B", A_B.getLastNodeName());
  }

  @Test
  public void testGetLength() {
    Assert.assertEquals(2, A_B.getLength());
  }

  @Test
  public void testGetName() {
    Assert.assertEquals("A", A_B.getName(0));
    Assert.assertEquals("B", A_B.getName(1));
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testGetName_OutOfBounds() {
    Assert.assertEquals("A", A_B.getName(2));
  }

  @Test
  public void testGetPath() {
    Assert.assertArrayEquals(new String[] { "A", "B" }, A_B.getPath());
  }

  @Test
  public void testOrder_Length() {
    assertOrder(0, A, A2);
    assertOrder(-1, A, B);
    assertOrder(1, B, A);
  }

  @Test
  public void testOrder_Longer() {
    assertOrder(-1, A, A_B);
    assertOrder(1, B, A_B);
  }

  @Test
  public void testOrder_SameInstance() {
    assertOrder(0, A, A);
  }

  @Test
  public void testOrder_Shorter() {
    assertOrder(1, A_B, A);
    assertOrder(1, A, Path.EMPTY);
  }

  @Test
  public void testToString() {
    Assert.assertEquals("Path[path=[A, B]]", A_B.toString());
  }

  /**
   * Assert equals, hashcode and compareTo.
   */
  private void assertOrder(final int expected, final Path a, final Path b) {
    // when
    final boolean equals = a.equals(b);
    final int hashCodeA = a.hashCode();
    final int hashCodeB = b.hashCode();
    final int compareTo = a.compareTo(b);
    // then
    if (expected < 0) {
      // a < b
      Assert.assertFalse(equals);
      Assert.assertFalse(hashCodeA == hashCodeB);
      Assert.assertTrue(compareTo < 0);
    } else if (expected == 0) {
      // a == b
      Assert.assertTrue(equals);
      Assert.assertTrue(hashCodeA == hashCodeB);
      Assert.assertTrue(expected == compareTo);
    } else if (expected > 0) {
      // a > b
      Assert.assertFalse(equals);
      Assert.assertFalse(hashCodeA == hashCodeB);
      Assert.assertTrue(compareTo > 0);
    }
  }
}
