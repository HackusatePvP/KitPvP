package cc.fatenetwork.kitpvp.utils;

import java.util.*;

public final class GenericUtils{
	public static <E> List<E> createList(Object object, Class<E> type){
		ArrayList<E> output = new ArrayList<E>();
		if(object != null && object instanceof List){
			List input = (List) object;
			for(Object value : input){
				if(value == null || value.getClass() == null) continue;
				if(!type.isAssignableFrom(value.getClass())){
					String simpleName = type.getSimpleName();
					throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
				}
				E e = type.cast(value);
				output.add(e);
			}
		}
		return output;
	}

	public static <E> Set<E> castSet(Object object, Class<E> type){
		HashSet<E> output = new HashSet<E>();
		if(object != null && object instanceof List){
			List input = (List) object;
			for(Object value : input){
				if(value == null || value.getClass() == null) continue;
				if(!type.isAssignableFrom(value.getClass())){
					String simpleName = type.getSimpleName();
					throw new AssertionError("Cannot cast to list! Key " + value + " is not a " + simpleName);
				}
				E e = type.cast(value);
				output.add(e);
			}
		}
		return output;
	}

	public static <K, V> Map<K, V> castMap(Object object, Class<K> keyClass, Class<V> valueClass){
		HashMap<K, V> output = new HashMap<K, V>();
		if(object != null && object instanceof Map){
			Map input = (Map) object;
			String keyClassName = keyClass.getSimpleName();
			String valueClassName = valueClass.getSimpleName();
			for(Object key : input.keySet().toArray()){
				if(key != null && !keyClass.isAssignableFrom(key.getClass())){
					throw new AssertionError("Cannot cast to HashMap: " + keyClassName + ", " + keyClassName + ". Value " + valueClassName + " is not a " + keyClassName);
				}
				Object value = input.get(key);
				if(value != null && !valueClass.isAssignableFrom(value.getClass())){
					throw new AssertionError("Cannot cast to HashMap: " + valueClassName + ", " + valueClassName + ". Key " + key + " is not a " + valueClassName);
				}
				output.put(keyClass.cast(key), valueClass.cast(value));
			}
		}
		return output;
	}
}
