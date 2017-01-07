package nl.jvdploeg.flat.annotated;

import nl.jvdploeg.flat.Model;
import nl.jvdploeg.flat.Path;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public abstract class AnnotatedModelUtils {

  private static class Description {
    private Path path;
    private String valueFieldName;
    private String[] childFieldNames;

    public Description(Path path, String valueFieldName, String[] childFieldNames) {
      this.path = path;
      this.valueFieldName = valueFieldName;
      this.childFieldNames = childFieldNames;
    }
  }

  private static final String CLOSING_BRACKET = "}";

  private static final String OPENING_BRACKET = "{";
  private static final String PATH_SEPARATOR = "/";

  /**
   * Add bean to model.
   * 
   * @param model
   *          The model.
   * @param bean
   *          The bean.
   */
  public static void add(Model model, Object bean) {
    // add node
    Description description = createDescription(bean);
    model.add(description.path);

    // add child nodes
    for (String childFieldName : description.childFieldNames) {
      Path childPath = description.path.createChildPath(childFieldName);
      model.add(childPath);
    }
  }

  /**
   * Get bean from model.
   * 
   * @param model
   *          The model.
   * @param reference
   *          The bean used as reference the location.
   * @return The bean.
   */
  public static <T> T get(Model model, T reference) {
    Description description = createDescription(reference);
    // collect constructor parameters
    List<Class<?>> parameterTypes = new ArrayList<>();
    List<Object> parameterValues = new ArrayList<>();
    // first parameter is the reference itself
    parameterTypes.add(reference.getClass());
    parameterValues.add(reference);
    // the second parameter is the value field (if present)
    if (description.valueFieldName != null) {
      String valueFieldValue = model.getValue(description.path);
      parameterTypes.add(String.class);
      parameterValues.add(valueFieldValue);
    }
    // then next parameters are the child fields
    for (String childFieldName : description.childFieldNames) {
      Path childPath = description.path.createChildPath(childFieldName);
      String childFieldValue = model.getValue(childPath);
      parameterTypes.add(String.class);
      parameterValues.add(childFieldValue);
    }
    // invoke matching constructor
    Class<?>[] parameterTypesArray = parameterTypes.toArray(new Class<?>[0]);
    Object[] parameterValuesArray = parameterValues.toArray();
    try {
      @SuppressWarnings("unchecked")
      Constructor<T> constructor = (Constructor<T>) reference.getClass()
          .getConstructor(parameterTypesArray);
      T newInstance = constructor.newInstance(parameterValuesArray);
      return newInstance;
    } catch (Exception ex) {
      throw new IllegalArgumentException(ex);
    }
  }

  /**
   * Remove bean from model.
   * 
   * @param model
   *          The model.
   * @param bean
   *          The bean.
   */
  public static void remove(Model model, Object bean) {
    Description description = createDescription(bean);
    model.remove(description.path);
  }

  /**
   * Set bean in model.
   * 
   * @param model
   *          The model.
   * @param bean
   *          The bean.
   */
  public static void set(Model model, Object bean) {
    Description description = createDescription(bean);
    // set node value (if present)
    if (description.valueFieldName != null) {
      try {
        Object valueFieldValue = PropertyUtils.getProperty(bean, description.valueFieldName);
        model.setValue(description.path, (String) valueFieldValue);
      } catch (Exception ex) {
        throw new IllegalArgumentException(ex);
      }
    }
    // set child node values
    for (String childFieldName : description.childFieldNames) {
      Path childPath = description.path.createChildPath(childFieldName);
      try {
        Object childFieldValue = PropertyUtils.getProperty(bean, childFieldName);
        model.setValue(childPath, (String) childFieldValue);
      } catch (Exception ex) {
        throw new IllegalArgumentException(ex);
      }
    }
  }

  private static Description createDescription(Object bean) {
    Class<? extends Object> beanClass = bean.getClass();
    Node[] annotations = beanClass.getAnnotationsByType(Node.class);
    if (annotations.length != 1) {
      throw new IllegalArgumentException(
          "expected one " + Node.class.getSimpleName() + " annotation, got " + annotations.length);
    }
    Node annotation = annotations[0];
    String pathExpression = annotation.path();
    Path path = createPath(bean, pathExpression);
    String valueFieldName = annotation.value();
    String[] childFieldNames = annotation.children();
    Description description = new Description(path, valueFieldName, childFieldNames);
    return description;
  }

  private static Path createPath(Object bean, String pathExpression) {
    String[] elements = pathExpression.split(PATH_SEPARATOR);
    // find symbolic elements
    for (int i = 0; i < elements.length; i++) {
      String element = elements[i];
      if (element.startsWith(OPENING_BRACKET) && element.endsWith(CLOSING_BRACKET)) {
        // strip surrounding brackets
        String field = element.substring(1, element.length() - 1);
        try {
          String value = BeanUtils.getProperty(bean, field);
          elements[i] = value;
        } catch (Exception ex) {
          throw new IllegalArgumentException(ex);
        }

      }
    }
    return new Path(elements);
  }
}
