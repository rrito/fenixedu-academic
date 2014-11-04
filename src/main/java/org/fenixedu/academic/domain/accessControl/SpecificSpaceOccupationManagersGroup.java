package org.fenixedu.academic.domain.accessControl;

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

@GroupOperator("specificSpaceOccupationManagersGroup")
public class SpecificSpaceOccupationManagersGroup extends FenixGroupStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public Set<User> getMembers() {
        return SpaceUtils.allocatableSpaces().map(s -> s.getOccupationsGroupWithChainOfResponsability()).filter(g -> g != null)
                .flatMap(g -> g.getMembers().stream()).collect(Collectors.toSet());
    }

    @Override
    public boolean isMember(User user) {
        Person person = user.getPerson();
        if (person != null && person.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
            return true;
        }
        for (Space space : SpaceUtils.allocatableSpaces().collect(Collectors.toSet())) {
            if (space.isOccupationMember(user)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }
}
