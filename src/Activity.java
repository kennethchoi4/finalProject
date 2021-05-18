public class Activity implements Action{
    private ActiveEntity entity;
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(
            ActiveEntity entity,
            WorldModel world,
            ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;

    }

    public void executeAction(EventScheduler scheduler){
//        try{
//            Executable e = (Executable)this.entity;
//            e.executeActivity(this.world, this.imageStore, scheduler);
//        }catch(){
//            System.out.println("catch: " + entity.getClass().toString());
//        }

        this.entity.executeActivity(this.world, this.imageStore, scheduler);


        /* switch (this.entity.kind()) {
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
        } */
    }
}
