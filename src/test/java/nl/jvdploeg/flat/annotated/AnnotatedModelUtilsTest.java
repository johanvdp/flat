package nl.jvdploeg.flat.annotated;

import nl.jvdploeg.flat.Enforcement;
import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import org.junit.Assert;
import org.junit.Test;

public class AnnotatedModelUtilsTest {

  @Test
  public void testAdd() {
    Model model = new Model("test", Enforcement.STRICT);
    model.add(new Path("Cars"));

    Car car = new Car("1", "value", "2");

    AnnotatedModelUtils.add(model, car);

    Assert.assertNotNull(model.getNode(new Path(new String[] { "Cars", "1" })));
    Assert.assertNotNull(model.getNode(new Path(new String[] { "Cars", "1", "lane" })));
  }

  @Test
  public void testGet() {
    Model model = new Model("test", Enforcement.STRICT);
    model.add(new Path("Cars"));
    model.add(new Path(new String[] { "Cars", "1" }));
    model.add(new Path(new String[] { "Cars", "1", "lane" }));
    Car set = new Car("1", "value", "2");
    AnnotatedModelUtils.set(model, set);

    Car get = AnnotatedModelUtils.get(model, new Car("1"));

    Assert.assertEquals("1", get.getId());
    Assert.assertEquals("value", get.getValue());
    Assert.assertEquals("2", get.getLane());
  }

  @Test
  public void testSet() {
    Model model = new Model("test", Enforcement.STRICT);
    model.add(new Path("Cars"));
    model.add(new Path(new String[] { "Cars", "1" }));
    model.add(new Path(new String[] { "Cars", "1", "lane" }));

    Car car = new Car("1", "value", "2");
    AnnotatedModelUtils.set(model, car);

    Assert.assertEquals("value", model.getValue(new Path(new String[] { "Cars", "1" })));
    Assert.assertEquals("2", model.getValue(new Path(new String[] { "Cars", "1", "lane" })));
  }
}
