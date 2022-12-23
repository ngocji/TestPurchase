package ji.purchase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.HashMap;

public class IPurchaseLiveEvent<T> extends MutableLiveData<T> {
    private HashMap<Integer, Boolean> pending = new HashMap<>();

    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, new InnerObserver<T>(observer));
    }

    @Override
    public void postValue(T value) {
        for (int key : pending.keySet()) {
            pending.put(key, true);
        }
        super.postValue(value);
    }

    public void post() {
        postValue(null);
    }

    @Override
    public void removeObserver(@NonNull Observer<? super T> observer) {
        super.removeObserver(observer);
        pending.remove(observer.hashCode());
    }

    public void notifyDataChanged() {
        postValue(getValue());
    }

    private class InnerObserver<T> implements Observer<T> {
        private Observer<? super T> observer;

        public InnerObserver(Observer<? super T> observer) {
            pending.put(hashCode(), false);
            this.observer = observer;
        }

        @Override
        public void onChanged(T t) {
            boolean shouldNotify = pending.containsKey(hashCode()) ? pending.get(hashCode()) : false;
            if (shouldNotify) {
                observer.onChanged(t);
                pending.put(hashCode(), false);
            }
        }
    }
}
