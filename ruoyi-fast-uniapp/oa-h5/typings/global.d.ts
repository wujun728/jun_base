interface PageLoadOptions {
  id: string;
}

interface EventDetails {
  value: any;
}

interface UniCustomEvent extends Event {
  detail: EventDetails;
}

type EventHandler<T> = (e: T) => void;
type UniEventValueHandler = EventHandler<UniCustomEvent>;

interface EchartIndexEvent extends Event {
  currentIndex: number;
  id: string;
  seriesName: string;
  type: string;
  value: {
    id: number;
    name: string;
    value: number;
  };
}
