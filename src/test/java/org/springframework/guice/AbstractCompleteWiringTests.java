/*
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.guice;

import javax.inject.Inject;
import javax.inject.Named;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractCompleteWiringTests {

	private Injector injector;

	@BeforeEach
	public void init() {
		this.injector = createInjector();
	}

	protected abstract Injector createInjector();

	@Test
	public void injectInstance() {
		Bar bar = new Bar();
		this.injector.injectMembers(bar);
		assertNotNull(bar.service);
	}

	@Test
	public void memberInjector() {
		Bar bar = new Bar();
		this.injector.getMembersInjector(Bar.class).injectMembers(bar);
		assertNotNull(bar.service);
	}

	@Test
	public void getInstanceUnbound() {
		assertNotNull(this.injector.getInstance(Foo.class));
	}

	@Test
	public void getInstanceBound() {
		assertNotNull(this.injector.getInstance(Service.class));
	}

	@Test
	public void getInstanceBoundWithNoInterface() {
		Baz instance = this.injector.getInstance(Baz.class);
		assertNotNull(instance);
		assertEquals(instance, this.injector.getInstance(Baz.class));
	}

	@Test
	public void getProviderUnbound() {
		assertNotNull(this.injector.getProvider(Foo.class).get());
	}

	@Test
	public void getProviderBound() {
		assertNotNull(this.injector.getProvider(Service.class).get());
	}

	@Test
	public void getNamedInstance() {
		assertNotNull(this.injector.getInstance(Key.get(Thang.class, Names.named("thing"))));
	}

	@Test
	public void getNamedInjectedInstance() {
		assertNotNull(this.injector.getInstance(Thing.class).thang);
	}

	@Test
	public void getParameterizedType() {
		Parameterized<String> instance = this.injector.getInstance(Key.get(new TypeLiteral<Parameterized<String>>() {
		}));
		assertNotNull(instance);
	}

	public interface Service {

	}

	public static class MyService implements Service {

	}

	public static class Foo {

		@Inject
		public Foo(Service service) {
		}

	}

	public static class Bar {

		private Service service;

		@Inject
		public void setService(Service service) {
			this.service = service;
		}

	}

	public static class Baz {

		@Inject
		public Baz(Service service) {
		}

	}

	public static class Thing {

		private Thang thang;

		@Inject
		public void setThang(@Named("thing") Thang thang) {
			this.thang = thang;
		}

	}

	public static class Thang {

	}

	public interface Parameterized<T> {

	}

}
