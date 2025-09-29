package com.eat.today.sns.query.service.dm;

import com.eat.today.sns.query.dto.dm.DmDTO;
import com.eat.today.sns.query.repository.dm.DmMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DmServiceImpl implements DmService{
    private DmMapper DmMapper;

    @Autowired
    public DmServiceImpl(DmMapper dmMapper) {
        this.DmMapper = dmMapper;
    }

    @Override
    public List<DmDTO> getByMessageNo(int sendMemberId, int receiveMemberId) {
        return DmMapper.selectByMessageNo(sendMemberId, receiveMemberId);
    }
}
