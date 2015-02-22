package com.artemis;

import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.artemis.utils.IntBag;


/**
 * The most raw entity system.
 * <p>
 * It should not typically be used, but you can create your own entity system
 * handling by extending this.
 * </p>
 *
 * @author Arni Arent
 */
public abstract class EntitySystem extends BaseSystem
		implements EntitySubscription.SubscriptionListener {

	private final Aspect.Builder aspectConfiguration;
	protected IntBag actives;
	protected Entity flyweight;

	/**
	 * Creates an entity system that uses the specified aspect as a matcher
	 * against entities.
	 *
	 * @param aspect
	 *			to match against entities
	 */
	public EntitySystem(Aspect.Builder aspect) {
		super();
		if (aspect == null) {
			String error = "Aspect.Builder was null; to use systems which " +
					"do not subscribe to entities, extend VoidEntitySystem or " +
					"BaseSystem directly.";
			throw new NullPointerException(error);
		}

		aspectConfiguration = aspect;
	}

	protected void setWorld(World world) {
		super.setWorld(world);

		EntitySubscription subscription = getSubscription();
		subscription.addSubscriptionListener(this);
		actives = subscription.getEntities();

		flyweight = Entity.createFlyweight(world);
	}

	public EntitySubscription getSubscription() {
		AspectSubscriptionManager sm = world.getManager(AspectSubscriptionManager.class);
		return sm.get(aspectConfiguration);
	}

	@Override
	public void inserted(ImmutableBag<Entity> entities) {
		Object[] data = ((Bag<Entity>)entities).getData();
		for (int i = 0, s = entities.size(); s > i; i++) {
			inserted((Entity) data[i]);
		}
	}

	/**
	 * Called if the system has received a entity it is interested in, e.g
	 * created or a component was added to it.
	 *
	 * @param e
	 *			the entity that was added to this system
	 */
	protected void inserted(Entity e) {}

	@Override
	public void removed(ImmutableBag<Entity> entities) {
		Object[] data = ((Bag<Entity>)entities).getData();
		for (int i = 0, s = entities.size(); s > i; i++) {
			removed((Entity) data[i]);
		}
	}

	/**
	 * Called if a entity was removed from this system, e.g deleted or had one
	 * of it's components removed.
	 *
	 * @param e
	 *			the entity that was removed from this system
	 */
	protected void removed(Entity e) {}


	/**
	 * Get all entities being processed by this system.
	 *
	 * @return a bag containing all active entities of the system
	 *
	 * @deprecated Retrieve the entities from the {@link com.artemis.EntitySubscription}
	 * directly.
	 */
	@Deprecated
	public Bag<Entity> getActives(Bag<Entity> fillBag) {
		if (actives == null)
			return fillBag;

		int[] array = actives.getData();
		for (int i = 0, s = actives.size(); s > i; i++) {
			fillBag.add(world.getEntity(array[i]));
		}

		return fillBag;
	}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	protected final void check(Entity e) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void added(Entity e) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void added(ImmutableBag<Entity> entities) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void changed(ImmutableBag<Entity> entities) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void deleted(ImmutableBag<Entity> entities) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void changed(Entity e) {}

	/**
	 * This method no longer performs any operations due to entity subscriptions lists
	 * being refactored into {@link com.artemis.AspectSubscriptionManager} and
	 * {@link com.artemis.EntitySubscription}.
	 */
	@Deprecated
	public final void deleted(Entity e) {}

	/**
	 * Call when an entity interesting to the system was disabled.
	 *
	 * <p>
	 * If the disabled entity is in this system it will be removed
	 * </p>
	 *
	 * @param e
	 *			the disabled entity
	 */
	@Deprecated
	public final void disabled(Entity e) {}

	/**
	 * Call when an entity interesting to the system was (re)enabled.
	 * <p>
	 * If the enabled entity is of interest, in will be (re)inserted.
	 * </p>
	 *
	 * @param e
	 *			the (re)enabled entity
	 */
	@Deprecated
	public final void enabled(Entity e) {}

	/**
	 * Get all entities being processed by this system.
	 *
	 * @return a bag containing all active entities of the system
	 * @deprecated This method allocates a new Bag each time, refer to {@link #getActives(com.artemis.utils.Bag)}
	 */
	@Deprecated
	public ImmutableBag<Entity> getActives() {
		return getActives(new Bag<Entity>());
	}
}
