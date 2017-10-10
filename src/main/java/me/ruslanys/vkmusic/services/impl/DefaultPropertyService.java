package me.ruslanys.vkmusic.services.impl;

import lombok.NonNull;
import me.ruslanys.vkmusic.entity.Property;
import me.ruslanys.vkmusic.property.DownloaderProperties;
import me.ruslanys.vkmusic.property.Properties;
import me.ruslanys.vkmusic.repository.PropertyRepository;
import me.ruslanys.vkmusic.services.PropertyService;
import me.ruslanys.vkmusic.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @author Ruslan Molchanov (ruslanys@gmail.com)
 */
@Transactional
@Service
@DependsOn("jsonUtils")
public class DefaultPropertyService implements PropertyService {

    private final PropertyRepository propertyRepository;

    @Autowired
    public DefaultPropertyService(@NonNull PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @PostConstruct
    private void init() {
        if (get(DownloaderProperties.class) == null) {
            set(new DownloaderProperties());
        }
    }

    @Override
    public void set(Properties properties) {
        Property entity = new Property(properties.getClass().getSimpleName(), JsonUtils.toString(properties));
        propertyRepository.save(entity);
    }

    @Override
    public <T extends Properties> T get(Class<T> clazz) {
        Property entity = propertyRepository.findOne(clazz.getSimpleName());
        if (entity != null) {
            return JsonUtils.fromString(entity.getJson(), clazz);
        }

        return null;
    }

    @Override
    public <T extends Properties> void remove(Class<T> clazz) {
        String key = clazz.getSimpleName();
        if (propertyRepository.exists(key)) {
            propertyRepository.delete(key);
        }
    }

}