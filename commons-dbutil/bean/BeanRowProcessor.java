package com.kk.dbutils.bean;

import org.apache.commons.dbutils.RowProcessor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BeanRowProcessor implements RowProcessor {

    /**
     * The default BeanProcessor instance to use if not supplied in the
     * constructor.
     */
    private static final BeanTplProcessor defaultConvert = new BeanTplProcessor();


    /**
     * Use this to process beans.
     */
    private final BeanTplProcessor convert;

    /**
     * BasicRowProcessor constructor.  Bean processing defaults to a
     * BeanProcessor instance.
     */
    public BeanRowProcessor() {
        this(defaultConvert);
    }

    /**
     * BasicRowProcessor constructor.
     *
     * @param convert The BeanProcessor to use when converting columns to
     *                bean properties.
     * @since DbUtils 1.1
     */
    public BeanRowProcessor(BeanTplProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Convert a <code>ResultSet</code> row into an <code>Object[]</code>.
     * This implementation copies column values into the array in the same
     * order they're returned from the <code>ResultSet</code>.  Array elements
     * will be set to <code>null</code> if the column was SQL NULL.
     *
     * @param rs ResultSet that supplies the array data
     * @return the newly created array
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.RowProcessor#toArray(java.sql.ResultSet)
     */
    @Override
    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    }

    /**
     * Convert a <code>ResultSet</code> row into a JavaBean.  This
     * implementation delegates to a BeanProcessor instance.
     *
     * @param <T>  The type of bean to create
     * @param rs   ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @return the newly created bean
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.RowProcessor#toBean(java.sql.ResultSet, java.lang.Class)
     * @see org.apache.commons.dbutils.BeanProcessor#toBean(java.sql.ResultSet, java.lang.Class)
     */
    @Override
    public <T> T toBean(ResultSet rs, Class<T> type) throws SQLException {
        return this.convert.toBean(rs, type);
    }

    /**
     * Convert a <code>ResultSet</code> into a <code>List</code> of JavaBeans.
     * This implementation delegates to a BeanProcessor instance.
     *
     * @param <T>  The type of bean to create
     * @param rs   ResultSet that supplies the bean data
     * @param type Class from which to create the bean instance
     * @return A <code>List</code> of beans with the given type in the order
     * they were returned by the <code>ResultSet</code>.
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.RowProcessor#toBeanList(java.sql.ResultSet, java.lang.Class)
     * @see org.apache.commons.dbutils.BeanProcessor#toBeanList(java.sql.ResultSet, java.lang.Class)
     */
    @Override
    public <T> List<T> toBeanList(ResultSet rs, Class<T> type) throws SQLException {
        return this.convert.toBeanList(rs, type);
    }

    /**
     * Convert a <code>ResultSet</code> row into a <code>Map</code>.
     * <p/>
     * <p>
     * This implementation returns a <code>Map</code> with case insensitive column names as keys. Calls to
     * <code>map.get("COL")</code> and <code>map.get("col")</code> return the same value. Furthermore this implementation
     * will return an ordered map, that preserves the ordering of the columns in the ResultSet, so that iterating over
     * the entry set of the returned map will return the first column of the ResultSet, then the second and so forth.
     * </p>
     *
     * @param rs ResultSet that supplies the map data
     * @return the newly created Map
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.RowProcessor#toMap(java.sql.ResultSet)
     */
    @Override
    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> result = new CaseInsensitiveHashMap();
        ResultSetMetaData rsmd = rs.getMetaData();
        int cols = rsmd.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            String columnName = rsmd.getColumnLabel(i);
            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(i);
            }
            result.put(columnName, rs.getObject(i));
        }

        return result;
    }

    /**
     * A Map that converts all keys to lowercase Strings for case insensitive
     * lookups.  This is needed for the toMap() implementation because
     * databases don't consistently handle the casing of column names.
     * <p/>
     * <p>The keys are stored as they are given [BUG #DBUTILS-34], so we maintain
     * an internal mapping from lowercase keys to the real keys in order to
     * achieve the case insensitive lookup.
     * <p/>
     * <p>Note: This implementation does not allow <tt>null</tt>
     * for key, whereas {@link LinkedHashMap} does, because of the code:
     * <pre>
     * key.toString().toLowerCase()
     * </pre>
     */
    private static class CaseInsensitiveHashMap extends LinkedHashMap<String, Object> {
        /**
         * The internal mapping from lowercase keys to the real keys.
         * <p/>
         * <p>
         * Any query operation using the key
         * ({@link #get(Object)}, {@link #containsKey(Object)})
         * is done in three steps:
         * <ul>
         * <li>convert the parameter key to lower case</li>
         * <li>get the actual key that corresponds to the lower case key</li>
         * <li>query the map with the actual key</li>
         * </ul>
         * </p>
         */
        private final Map<String, String> lowerCaseMap = new HashMap<String, String>();

        /**
         * Required for serialization support.
         *
         * @see java.io.Serializable
         */
        private static final long serialVersionUID = -2848100435296897392L;

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean containsKey(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.containsKey(realKey);
            // Possible optimisation here:
            // Since the lowerCaseMap contains a mapping for all the keys,
            // we could just do this:
            // return lowerCaseMap.containsKey(key.toString().toLowerCase());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object get(Object key) {
            Object realKey = lowerCaseMap.get(key.toString().toLowerCase(Locale.ENGLISH));
            return super.get(realKey);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object put(String key, Object value) {
            /*
             * In order to keep the map and lowerCaseMap synchronized,
             * we have to remove the old mapping before putting the
             * new one. Indeed, oldKey and key are not necessaliry equals.
             * (That's why we call super.remove(oldKey) and not just
             * super.put(key, value))
             */
            Object oldKey = lowerCaseMap.put(key.toLowerCase(Locale.ENGLISH), key);
            Object oldValue = super.remove(oldKey);
            super.put(key, value);
            return oldValue;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void putAll(Map<? extends String, ?> m) {
            for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                this.put(key, value);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Object remove(Object key) {
            Object realKey = lowerCaseMap.remove(key.toString().toLowerCase(Locale.ENGLISH));
            return super.remove(realKey);
        }
    }

}
