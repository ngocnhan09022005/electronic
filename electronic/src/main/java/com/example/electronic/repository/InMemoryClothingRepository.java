package com.example.electronic.repository;

import com.example.electronic.model.ClothingItem;
import com.example.electronic.model.ClothingType;
import jakarta.annotation.PostConstruct;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryClothingRepository implements ClothingRepository {

    private final ConcurrentMap<Long, ClothingItem> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public List<ClothingItem> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<ClothingItem> findByType(ClothingType type) {
        return storage.values().stream()
                .filter(item -> item.getType() == type)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClothingItem> findById(@NonNull Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    @NonNull
    public ClothingItem save(@NonNull ClothingItem item) {
        if (item.getId() == null) {
            item.setId(idGenerator.getAndIncrement());
        }
        storage.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteById(@NonNull Long id) {
        storage.remove(id);
    }

    @PostConstruct
    public void loadSamples() {
        save(new ClothingItem(null, "Áo sơ mi Oxford", "Áo tay dài kiểu dáng Hàn Quốc",
                BigDecimal.valueOf(520000), ClothingType.LONG_SLEEVE,
                "https://source.unsplash.com/random/400x400?shirt", true, 25));

        save(new ClothingItem(null, "Áo thun Oversize", "Chất cotton 100%, thấm hút tốt",
                BigDecimal.valueOf(280000), ClothingType.SHORT_SLEEVE,
                "https://source.unsplash.com/random/400x401?tshirt", true, 50));

        save(new ClothingItem(null, "Quần jean slim-fit", "Form ôm, co giãn 4 chiều",
                BigDecimal.valueOf(650000), ClothingType.LONG_PANTS,
                "https://source.unsplash.com/random/400x402?jeans", false, 15));

        save(new ClothingItem(null, "Quần short linen", "Mang phong cách resort",
                BigDecimal.valueOf(390000), ClothingType.SHORT_PANTS,
                "https://source.unsplash.com/random/400x403?shorts", false, 34));

        save(new ClothingItem(null, "Áo khoác denim", "Layer cá tính cho mùa thu",
                BigDecimal.valueOf(820000), ClothingType.OUTWEAR,
                "https://source.unsplash.com/random/400x404?jacket", true, 10));

        save(new ClothingItem(null, "Nón bucket canvas", "Phụ kiện đi biển hot trend",
                BigDecimal.valueOf(210000), ClothingType.ACCESSORY,
                "https://source.unsplash.com/random/400x405?hat", false, 65));
    }
}

