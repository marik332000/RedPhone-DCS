package org.whispersystems.data_collector;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;

import javax.ws.rs.ext.Provider;

import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

/**
 * Injects a data store.
 */
@Provider
public class DataStoreProvider implements InjectableProvider<DataStoreProvider.DataStoreResource, Type>{
  @Retention(RetentionPolicy.RUNTIME)
  public @interface DataStoreResource {}

  @Override
  public ComponentScope getScope() {
    return ComponentScope.Singleton;
  }

  @Override
  public Injectable getInjectable(ComponentContext componentContext, DataStoreResource dataStoreResource, Type type) {
    if(DataStore.class.equals(type)) {
      return new Injectable<DataStore>() {
        @Override
        public DataStore getValue() {
          if("AMAZON_S3".equals(System.getenv("REDPHONE_DATASTORE"))) {
            return new S3DataStore();
          } else {
            return new FileSystemDataStore(new File("./uploads"));
          }
        }
      };
    } else {
      return null;
    }
  }
}
