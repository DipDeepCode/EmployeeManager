package ru.ddc.em.utils.custommapper;

import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@NoArgsConstructor
@Component
public class CustomMapper {
    private ModelMapper modelMapper;

    @Autowired
    public CustomMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> T map(S source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }

//    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
//        return source.stream()
//                .map(element -> modelMapper.map(element, targetClass))
//                .toList();
//    }

    public <S, T> List<T> mapIterable(Iterable<S> source, Class<T> targetClass) {
        return StreamSupport.stream(source.spliterator(), false)
                .map(element -> modelMapper.map(element, targetClass))
                .toList();
    }
}
