public final class Action
{
    private ActionKind kind;
    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;
    private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Action(
            ActionKind kind,
            Entity entity,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler) {
        if (this.kind.equals(ActionKind.ACTIVITY)){
                this.executeActivityAction(scheduler);
        } else if (this.kind.equals(ActionKind.ANIMATION)){
            this.executeAnimationAction(scheduler);
        }
    }

    private void executeActivityAction(EventScheduler scheduler)
    {
        switch (this.entity.kind()) {
            case MINER_FULL:
                this.entity.executeMinerFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case MINER_NOT_FULL:
                this.entity.executeMinerNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case ORE:
                this.entity.executeOreActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case ORE_BLOB:
                this.entity.executeOreBlobActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                this.entity.executeQuakeActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case VEIN:
                this.entity.executeVeinActivity(this.world,
                        this.imageStore, scheduler);
                break;

            default:
                throw new UnsupportedOperationException(String.format(
                        "executeActivityAction not supported for %s",
                        this.entity.kind()));
        }
    }

    private void executeAnimationAction(EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    Functions.createAnimationAction(this.entity,
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }

    public static void scheduleActions(
            Entity entity,
            EventScheduler scheduler,
            WorldModel world,
            ImageStore imageStore)
    {
        switch (entity.kind()) {
            case MINER_FULL:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                scheduler.scheduleEvent(entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case MINER_NOT_FULL:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                scheduler.scheduleEvent(entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case ORE:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                break;

            case ORE_BLOB:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                scheduler.scheduleEvent(entity,
                        Functions.createAnimationAction(entity, 0),
                        entity.getAnimationPeriod());
                break;

            case QUAKE:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                scheduler.scheduleEvent(entity, Functions.createAnimationAction(entity,
                        QUAKE_ANIMATION_REPEAT_COUNT),
                        entity.getAnimationPeriod());
                break;

            case VEIN:
                scheduler.scheduleEvent(entity,
                        Functions.createActivityAction(entity, world, imageStore),
                        entity.actionPeriod());
                break;

            default:
        }
    }
}
