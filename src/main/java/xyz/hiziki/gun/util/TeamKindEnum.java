package xyz.hiziki.gun.util;

import org.bukkit.ChatColor;

public enum TeamKindEnum
{
    Red("team_red","赤", ChatColor.RED),
    Blue("team_blue","青",ChatColor.BLUE);

    private final String teamName;
    private final String viewTeamName;
    private final ChatColor teamColor;

    TeamKindEnum(String _teamName,String _viewTeamName, ChatColor _teamColor)
    {
        teamName = _teamName;
        viewTeamName = _viewTeamName;
        teamColor = _teamColor;
    }

    public String getTeamName()
    {
        return teamName;
    }

    public String getViewTeamName()
    {
        return viewTeamName + "チーム";
    }

    public ChatColor getTeamColor()
    {
        return teamColor;
    }

    public String getPrefix()
    {
        return "【"+this.getViewTeamName()+"】";
    }

    public static TeamKindEnum getEnum(String teamName)
    {
        for (TeamKindEnum team : TeamKindEnum.values() )
        {
            if (team.getTeamName().equals(teamName))
            {
                return team;
            }
        }
        return null;
    }
}